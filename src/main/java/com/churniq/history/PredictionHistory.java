
package com.churniq.history;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "prediction_history")
public class PredictionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String prediction;

    private String message;

    private Double tenure;

    private Double monthlyCharges;

    private Double totalCharges;

    private LocalDateTime predictedAt;

    public PredictionHistory() {
    }

    @PrePersist
    public void setPredictionTime() {
        predictedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Double getTenure() {
        return tenure;
    }

    public void setTenure(Double tenure) {
        this.tenure = tenure;
    }

    public Double getMonthlyCharges() {
        return monthlyCharges;
    }

    public void setMonthlyCharges(Double monthlyCharges) {
        this.monthlyCharges = monthlyCharges;
    }

    public Double getTotalCharges() {
        return totalCharges;
    }

    public void setTotalCharges(Double totalCharges) {
        this.totalCharges = totalCharges;
    }

    public LocalDateTime getPredictedAt() {
        return predictedAt;
    }
}
