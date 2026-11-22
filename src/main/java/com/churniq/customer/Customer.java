package com.churniq.customer;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "customers")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "customerId is required")
	@Size(max = 64, message = "customerId must be at most 64 characters")
	@Column(name = "customer_id", nullable = false, unique = true, length = 64)
	private String customerId;

	@NotBlank(message = "gender is required")
	@Size(max = 50, message = "gender must be at most 50 characters")
	@Column(nullable = false, length = 50)
	private String gender;

	@NotNull(message = "tenure is required")
	@Min(value = 0, message = "tenure must be zero or greater")
	@Column(nullable = false)
	private Integer tenure;

	@NotBlank(message = "contract is required")
	@Size(max = 50, message = "contract must be at most 50 characters")
	@Column(nullable = false, length = 50)
	private String contract;

	@NotNull(message = "monthlyCharges is required")
	@DecimalMin(value = "0.00", message = "monthlyCharges must be zero or greater")
	@Digits(integer = 8, fraction = 2, message = "monthlyCharges must have at most 8 digits and 2 decimal places")
	@Column(name = "monthly_charges", nullable = false, precision = 10, scale = 2)
	private BigDecimal monthlyCharges;

	@NotNull(message = "totalCharges is required")
	@DecimalMin(value = "0.00", message = "totalCharges must be zero or greater")
	@Digits(integer = 8, fraction = 2, message = "totalCharges must have at most 8 digits and 2 decimal places")
	@Column(name = "total_charges", nullable = false, precision = 10, scale = 2)
	private BigDecimal totalCharges;

	@NotNull(message = "churned is required")
	@Column(nullable = false)
	private Boolean churned;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getTenure() {
		return tenure;
	}

	public void setTenure(Integer tenure) {
		this.tenure = tenure;
	}

	public String getContract() {
		return contract;
	}

	public void setContract(String contract) {
		this.contract = contract;
	}

	public BigDecimal getMonthlyCharges() {
		return monthlyCharges;
	}

	public void setMonthlyCharges(BigDecimal monthlyCharges) {
		this.monthlyCharges = monthlyCharges;
	}

	public BigDecimal getTotalCharges() {
		return totalCharges;
	}

	public void setTotalCharges(BigDecimal totalCharges) {
		this.totalCharges = totalCharges;
	}

	public Boolean getChurned() {
		return churned;
	}

	public void setChurned(Boolean churned) {
		this.churned = churned;
	}

}
