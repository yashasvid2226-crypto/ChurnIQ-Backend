
package com.churniq.history;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class PredictionHistoryService {

    private final PredictionHistoryRepository repository;

    public PredictionHistoryService(PredictionHistoryRepository repository) {
        this.repository = repository;
    }

    public PredictionHistory savePrediction(
            String prediction,
            String message,
            Double tenure,
            Double monthlyCharges,
            Double totalCharges) {

        PredictionHistory history = new PredictionHistory();

        history.setPrediction(prediction);
        history.setMessage(message);
        history.setTenure(tenure);
        history.setMonthlyCharges(monthlyCharges);
        history.setTotalCharges(totalCharges);

        return repository.save(history);
    }

    public List<PredictionHistory> getAllPredictions() {
        return repository.findAll();
    }
}
