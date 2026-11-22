
package com.churniq.ml;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.churniq.history.PredictionHistoryService;

@RestController
@RequestMapping("/api/predictions")
public class PredictionController {

    private final ChurnPredictionService predictionService;
    private final PredictionHistoryService historyService;

    public PredictionController(
            ChurnPredictionService predictionService,
            PredictionHistoryService historyService) {

        this.predictionService = predictionService;
        this.historyService = historyService;
    }

    @PostMapping
    public ResponseEntity<PredictionResponse> predict(
            @RequestBody PredictionRequest request) {

        int result = predictionService.predict(request);

        String prediction = result == 1 ? "Yes" : "No";

        String message = result == 1
                ? "Customer is likely to churn."
                : "Customer is likely to stay.";

        // Save prediction in MySQL
        historyService.savePrediction(
                prediction,
                message,
                request.getTenure(),
                request.getMonthlyCharges(),
                request.getTotalCharges()
        );

        return ResponseEntity.ok(
                new PredictionResponse(prediction, message)
        );
    }
}
