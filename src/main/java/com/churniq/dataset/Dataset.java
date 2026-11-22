package com.churniq.dataset;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "datasets")
public class Dataset {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "datasetName is required")
	@Size(max = 150, message = "datasetName must be at most 150 characters")
	@Column(name = "dataset_name", nullable = false, length = 150)
	private String datasetName;

	@NotNull(message = "totalRecords is required")
	@Min(value = 0, message = "totalRecords must be zero or greater")
	@Column(name = "total_records", nullable = false)
	private Integer totalRecords;

	@NotBlank(message = "status is required")
	@Size(max = 50, message = "status must be at most 50 characters")
	@Column(nullable = false, length = 50)
	private String status;

	@PastOrPresent(message = "uploadedAt cannot be in the future")
	@Column(name = "uploaded_at", nullable = false)
	private LocalDateTime uploadedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDatasetName() {
		return datasetName;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getUploadedAt() {
		return uploadedAt;
	}

	public void setUploadedAt(LocalDateTime uploadedAt) {
		this.uploadedAt = uploadedAt;
	}

}
