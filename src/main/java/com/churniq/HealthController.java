package com.churniq;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

	@GetMapping("/api/health")
	public HealthResponse health() {
		return new HealthResponse("UP", "ChurnIQ Backend", "MySQL");
	}

	public record HealthResponse(String status, String application, String database) {
	}

}
