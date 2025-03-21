package com.healthflow.dto;

import java.time.LocalDate;

public class MedicalRecordDTO {

    private Long id;
    private Long patientId;
    private String diagnosis;
    private String treatment;
    private LocalDate date;

    public MedicalRecordDTO(Long id, Long patientId, String diagnosis, String treatment, LocalDate date) {
        this.id = id;
        this.patientId = patientId;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
