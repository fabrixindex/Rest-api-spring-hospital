package com.healthflow.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
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

    @ManyToOne
    @JoinColumn(name= "medication_id", nullable = false)
    @JsonIgnoreProperties("prescriptions")
    private Medication medication;

    private LocalDate prescriptionDate;

    public Prescription() {}

    public Prescription(Patient patient, Doctor doctor, Medication medication, LocalDate prescriptionDate) {
        this.patient = patient;
        this.doctor = doctor;
        this.medication = medication;
        this.prescriptionDate = prescriptionDate;
    }

    public Long getId() { return id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public Medication getMedication() { return medication; }
    public void setMedication(Medication medication) { 
        this.medication = medication; 
    }

    public LocalDate getPrescriptionDate() { return prescriptionDate; }
    public void setPrescriptionDate(LocalDate prescriptionDate) { this.prescriptionDate = prescriptionDate; }

    @Override
    public String toString() {
        return "Prescription{" +
                "id=" + id +
                ", patient=" + (patient != null ? patient.getId() : "null") +
                ", doctor=" + (doctor != null ? doctor.getId() : "null") +
                ", medication=" + (medication != null ? medication.getId() : "null") +
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
