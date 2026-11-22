
package com.churniq.upload;

import com.churniq.customer.Customer;
import com.churniq.customer.CustomerRepository;
import com.churniq.dataset.Dataset;
import com.churniq.dataset.DatasetRepository;

import jakarta.validation.Validator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

@Service
public class UploadService {

    private final CustomerRepository customerRepository;
    private final DatasetRepository datasetRepository;
    private final Validator validator;

    public UploadService(CustomerRepository customerRepository,
                         DatasetRepository datasetRepository,
                         Validator validator) {
        this.customerRepository = customerRepository;
        this.datasetRepository = datasetRepository;
        this.validator = validator;
    }

    @Transactional
    public UploadResponse upload(Long datasetId, MultipartFile file)
            throws IOException {

        Dataset dataset = datasetRepository.findById(datasetId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Dataset not found"));

        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Please upload a non-empty CSV file");
        }

        int totalRows = 0;
        int imported = 0;
        int duplicatesSkipped = 0;
        int invalidRows = 0;

        Set<String> seenIds = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser parser = CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .setIgnoreEmptyLines(true)
                    .get()
                    .parse(reader)) {

            Set<String> headers = parser.getHeaderMap().keySet();

            String[] requiredHeaders = {
                "customerId", "gender", "tenure", "contract",
                "monthlyCharges", "totalCharges", "churned"
            };

            for (String header : requiredHeaders) {
                if (!headers.contains(header)) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Missing CSV column: " + header);
                }
            }

            for (CSVRecord record : parser) {
                totalRows++;

                try {
                    Customer customer = new Customer();

                    customer.setCustomerId(record.get("customerId").trim());
                    customer.setGender(record.get("gender").trim());
                    customer.setTenure(Integer.parseInt(record.get("tenure").trim()));
                    customer.setContract(record.get("contract").trim());

                    customer.setMonthlyCharges(
                            new BigDecimal(record.get("monthlyCharges").trim()));

                    customer.setTotalCharges(
                            new BigDecimal(record.get("totalCharges").trim()));

                    String churnValue = record.get("churned").trim();

                    if (!churnValue.equalsIgnoreCase("true")
                            && !churnValue.equalsIgnoreCase("false")) {
                        invalidRows++;
                        continue;
                    }

                    customer.setChurned(Boolean.parseBoolean(churnValue));

                    if (!validator.validate(customer).isEmpty()) {
                        invalidRows++;
                        continue;
                    }

                    String customerId = customer.getCustomerId();

                    if (!seenIds.add(customerId)
                            || customerRepository.existsByCustomerId(customerId)) {
                        duplicatesSkipped++;
                        continue;
                    }

                    customerRepository.save(customer);
                    imported++;

                } catch (RuntimeException e) {
                    invalidRows++;
                }
            }

        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Could not read the CSV file");
        }

        dataset.setTotalRecords(imported);
        dataset.setStatus("READY");
        datasetRepository.save(dataset);

        return new UploadResponse(
                totalRows, imported, duplicatesSkipped, invalidRows, "READY");
    }
}
