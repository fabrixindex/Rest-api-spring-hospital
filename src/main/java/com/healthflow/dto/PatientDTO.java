package com.healthflow.dto;

import com.healthflow.models.Patient;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "DTO representing a patient")
public record PatientDTO(

    @Schema(description = "First name of the patient", example = "John") String firstName,

    @Schema(description = "Last name of the patient", example = "Doe") String lastName,

    @Schema(description = "Date of birth of the patient in YYYY-MM-DD format", example = "1990-05-15") LocalDate dateOfBirth,

    @Schema(description = "Address of the patient", example = "123 Main Street, New York, NY") String address,

    @Schema(description = "Phone number of the patient", example = "+1 555-1234") String phone

) {
    public static PatientDTO fromEntity(Patient patient) {
        return new PatientDTO(
            patient.getFirstName(),
            patient.getLastName(),
            patient.getDateOfBirth(),
            patient.getAddress(),
            patient.getPhone()
        );
    }

    public Patient toEntity() {
        Patient patient = new Patient();
        patient.setFirstName(this.firstName);
        patient.setLastName(this.lastName);
        patient.setDateOfBirth(this.dateOfBirth);
        patient.setAddress(this.address);
        patient.setPhone(this.phone);
        return patient;
    }
}
