package com.churniq.dataset;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DatasetService {

	private final DatasetRepository datasetRepository;

	public DatasetService(DatasetRepository datasetRepository) {
		this.datasetRepository = datasetRepository;
	}

	@Transactional
	public Dataset register(Dataset dataset) {
		dataset.setId(null);
		if (dataset.getUploadedAt() == null) {
			dataset.setUploadedAt(LocalDateTime.now());
		}
		return datasetRepository.save(dataset);
	}

	@Transactional(readOnly = true)
	public List<Dataset> findAll() {
		return datasetRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Dataset findById(Long id) {
		return datasetRepository.findById(id)
				.orElseThrow(() -> new DatasetNotFoundException(id));
	}

	@Transactional
	public void delete(Long id) {
		if (!datasetRepository.existsById(id)) {
			throw new DatasetNotFoundException(id);
		}
		datasetRepository.deleteById(id);
	}

}
