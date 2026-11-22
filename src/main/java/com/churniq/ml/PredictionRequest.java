package com.churniq.ml;

public class PredictionRequest {

    private double gender;
    private double seniorCitizen;
    private double partner;
    private double dependents;
    private double tenure;
    private double phoneService;
    private double monthlyCharges;
    private double totalCharges;
    private double contract;
    private double internetService;

    public double getGender() { return gender; }
    public void setGender(double gender) { this.gender = gender; }

    public double getSeniorCitizen() { return seniorCitizen; }
    public void setSeniorCitizen(double seniorCitizen) { this.seniorCitizen = seniorCitizen; }

    public double getPartner() { return partner; }
    public void setPartner(double partner) { this.partner = partner; }

    public double getDependents() { return dependents; }
    public void setDependents(double dependents) { this.dependents = dependents; }

    public double getTenure() { return tenure; }
    public void setTenure(double tenure) { this.tenure = tenure; }

    public double getPhoneService() { return phoneService; }
    public void setPhoneService(double phoneService) { this.phoneService = phoneService; }

    public double getMonthlyCharges() { return monthlyCharges; }
    public void setMonthlyCharges(double monthlyCharges) { this.monthlyCharges = monthlyCharges; }

    public double getTotalCharges() { return totalCharges; }
    public void setTotalCharges(double totalCharges) { this.totalCharges = totalCharges; }

    public double getContract() { return contract; }
    public void setContract(double contract) { this.contract = contract; }

    public double getInternetService() { return internetService; }
    public void setInternetService(double internetService) { this.internetService = internetService; }
}