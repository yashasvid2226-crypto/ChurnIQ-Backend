package com.churniq.analytics;

public class AnalyticsResponse {

    private long totalCustomers;
    private long churnedCustomers;
    private long retainedCustomers;
    private double churnRate;
    private long totalDatasets;
    private long totalPredictions;

    public AnalyticsResponse(
            long totalCustomers,
            long churnedCustomers,
            long retainedCustomers,
            double churnRate,
            long totalDatasets,
            long totalPredictions) {

        this.totalCustomers = totalCustomers;
        this.churnedCustomers = churnedCustomers;
        this.retainedCustomers = retainedCustomers;
        this.churnRate = churnRate;
        this.totalDatasets = totalDatasets;
        this.totalPredictions = totalPredictions;
    }

    public long getTotalCustomers() {
        return totalCustomers;
    }

    public long getChurnedCustomers() {
        return churnedCustomers;
    }

    public long getRetainedCustomers() {
        return retainedCustomers;
    }

    public double getChurnRate() {
        return churnRate;
    }

    public long getTotalDatasets() {
        return totalDatasets;
    }

    public long getTotalPredictions() {
        return totalPredictions;
    }
}