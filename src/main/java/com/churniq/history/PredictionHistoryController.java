
package com.churniq.history;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/predictions/history")
public class PredictionHistoryController {

    private final PredictionHistoryService historyService;

    public PredictionHistoryController(
            PredictionHistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    public List<PredictionHistory> getAllPredictions() {
        return historyService.getAllPredictions();
    }
}
