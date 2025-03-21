package com.healthflow.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.Objects;

@Entity
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonIgnoreProperties("prescriptions") 
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    @JsonIgnoreProperties("prescriptions")
    private Doctor doctor;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
        name = "prescription_medications",
        joinColumns = @JoinColumn(name = "prescription_id"),
        inverseJoinColumns = @JoinColumn(name = "medication_id")
    )
    @JsonIgnoreProperties("prescriptions") 
    private Set<Medication> medications;

    private LocalDate prescriptionDate;

    public Prescription() {}

    public Prescription(Patient patient, Doctor doctor, Set<Medication> medications, LocalDate prescriptionDate) {
        this.patient = patient;
        this.doctor = doctor;
        this.medications = medications;
        this.prescriptionDate = prescriptionDate;
    }

    public Long getId() { return id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public Set<Medication> getMedications() { return medications; }
    public void setMedications(Set<Medication> medications) { this.medications = medications; }

    public LocalDate getPrescriptionDate() { return prescriptionDate; }
    public void setPrescriptionDate(LocalDate prescriptionDate) { this.prescriptionDate = prescriptionDate; }

    @Override
    public String toString() {
        return "Prescription{" +
                "id=" + id +
                ", patient=" + (patient != null ? patient.getId() : "null") +
                ", doctor=" + (doctor != null ? doctor.getId() : "null") +
                ", prescriptionDate=" + prescriptionDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prescription that = (Prescription) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
