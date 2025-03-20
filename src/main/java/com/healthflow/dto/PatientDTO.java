package com.healthflow.dto;

import com.healthflow.models.Patient;
import java.time.LocalDate;

public record PatientDTO(Long id, String firstName, String lastName, LocalDate dateOfBirth) {
    
    public static PatientDTO fromEntity(Patient patient) {
        return new PatientDTO(
            patient.getId(),
            patient.getFirstName(),
            patient.getLastName(),
            patient.getDateOfBirth()
        );
    }

    public Patient toEntity() {
        Patient patient = new Patient();
        patient.setId(this.id);
        patient.setFirstName(this.firstName);
        patient.setLastName(this.lastName);
        patient.setDateOfBirth(this.dateOfBirth);
        return patient;
    }
}
