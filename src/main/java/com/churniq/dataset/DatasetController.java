package com.churniq.dataset;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/datasets")
public class DatasetController {

	private final DatasetService datasetService;

	public DatasetController(DatasetService datasetService) {
		this.datasetService = datasetService;
	}

	@PostMapping
	public ResponseEntity<Dataset> register(@Valid @RequestBody Dataset dataset) {
		Dataset saved = datasetService.register(dataset);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(saved.getId())
				.toUri();
		return ResponseEntity.created(location).body(saved);
	}

	@GetMapping
	public List<Dataset> findAll() {
		return datasetService.findAll();
	}

	@GetMapping("/{id}")
	public Dataset findById(@PathVariable Long id) {
		return datasetService.findById(id);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		datasetService.delete(id);
	}

}
