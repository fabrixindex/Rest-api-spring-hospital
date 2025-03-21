package com.healthflow.dto;

import java.time.LocalDate;
import java.util.Set;

public class PrescriptionDTO {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private Set<Long> medicationIds;
    private LocalDate prescriptionDate;

    public PrescriptionDTO() {}

    public PrescriptionDTO(Long id, Long patientId, Long doctorId, Set<Long> medicationIds, LocalDate prescriptionDate) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.medicationIds = medicationIds;
        this.prescriptionDate = prescriptionDate;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    public Set<Long> getMedicationIds() { return medicationIds; }
    public void setMedicationIds(Set<Long> medicationIds) { this.medicationIds = medicationIds; }

    public LocalDate getPrescriptionDate() { return prescriptionDate; }
    public void setPrescriptionDate(LocalDate prescriptionDate) { this.prescriptionDate = prescriptionDate; }
}
