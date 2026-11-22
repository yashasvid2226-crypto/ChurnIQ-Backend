package com.churniq.ml;

public class PredictionResponse {

    private String prediction;
    private String message;

    public PredictionResponse(String prediction, String message) {
        this.prediction = prediction;
        this.message = message;
    }

    public String getPrediction() {
        return prediction;
    }

    public String getMessage() {
        return message;
    }
}