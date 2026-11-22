
package com.churniq.ml;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class TelcoDatasetLoader {

    private static final String DATASET_PATH =
            "src/main/resources/ml-data/Telco-Customer-Churn.csv";

    public static void main(String[] args) {

        File file = new File(DATASET_PATH);

        if (!file.exists()) {
            System.out.println("Dataset file not found!");
            return;
        }

        try (
            CSVParser parser = CSVParser.parse(
                file,
                StandardCharsets.UTF_8,
                CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .get()
            )
        ) {
            int totalCustomers = 0;
            int churnedCustomers = 0;
            int retainedCustomers = 0;
            int missingTotalCharges = 0;

            for (CSVRecord record : parser) {
                totalCustomers++;

                String churn = record.get("Churn").trim();
                String totalCharges = record.get("TotalCharges").trim();

                if (churn.equalsIgnoreCase("Yes")) {
                    churnedCustomers++;
                } else if (churn.equalsIgnoreCase("No")) {
                    retainedCustomers++;
                }

                if (totalCharges.isEmpty()) {
                    missingTotalCharges++;
                }
            }

            System.out.println("\n===== CHURNIQ DATASET LOADED =====");
            System.out.println("Total customers: " + totalCustomers);
            System.out.println("Churned customers: " + churnedCustomers);
            System.out.println("Retained customers: " + retainedCustomers);
            System.out.println("Missing TotalCharges: " + missingTotalCharges);
            System.out.println("\nDataset loaded successfully!");

        } catch (IOException e) {
            System.out.println("Error reading dataset: " + e.getMessage());
        }
    }
}
