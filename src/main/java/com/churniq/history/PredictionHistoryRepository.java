
package com.churniq.history;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PredictionHistoryRepository
        extends JpaRepository<PredictionHistory, Long> {
}
