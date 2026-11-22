package com.churniq.analytics;

import org.springframework.stereotype.Service;

import com.churniq.customer.CustomerRepository;
import com.churniq.dataset.DatasetRepository;
import com.churniq.history.PredictionHistoryRepository;

@Service
public class AnalyticsService {

    private final CustomerRepository customerRepository;
    private final DatasetRepository datasetRepository;
    private final PredictionHistoryRepository historyRepository;

    public AnalyticsService(
            CustomerRepository customerRepository,
            DatasetRepository datasetRepository,
            PredictionHistoryRepository historyRepository) {

        this.customerRepository = customerRepository;
        this.datasetRepository = datasetRepository;
        this.historyRepository = historyRepository;
    }

    public AnalyticsResponse getAnalytics() {

        long totalCustomers = customerRepository.count();

        long churnedCustomers =
                customerRepository.countByChurnedTrue();

        long retainedCustomers =
                totalCustomers - churnedCustomers;

        double churnRate = totalCustomers == 0
                ? 0.0
                : (churnedCustomers * 100.0) / totalCustomers;

        long totalDatasets = datasetRepository.count();

        long totalPredictions = historyRepository.count();

        return new AnalyticsResponse(
                totalCustomers,
                churnedCustomers,
                retainedCustomers,
                Math.round(churnRate * 100.0) / 100.0,
                totalDatasets,
                totalPredictions
        );
    }
}