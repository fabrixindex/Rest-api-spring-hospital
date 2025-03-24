package com.healthflow.dto;

import com.healthflow.models.Patient;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(description = "DTO representing a patient")
public record PatientDTO(
    
    @Schema(description = "ID of the patient", example = "1") 
    Long id,

    @NotBlank(message = "First name cannot be empty")
    @Size(max = 50, message = "First name must be at most 50 characters")
    @Schema(description = "First name of the patient", example = "John") 
    String firstName,

    @NotBlank(message = "Last name cannot be empty")
    @Size(max = 50, message = "Last name must be at most 50 characters")
    @Schema(description = "Last name of the patient", example = "Doe") 
    String lastName,

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    @Schema(description = "Date of birth of the patient in YYYY-MM-DD format", example = "1990-05-15") 
    LocalDate dateOfBirth,

    @NotBlank(message = "Gender cannot be empty")
    @Size(max = 10, message = "Gender must be at most 10 characters")
    @Schema(description = "Gender of the patient", example = "Male") 
    String gender,

    @Size(max = 255, message = "Address must be at most 255 characters")
    @Schema(description = "Address of the patient", example = "123 Main Street, New York, NY") 
    String address,

    @Pattern(regexp = "\\d{10}", message = "Phone number must have exactly 10 digits")
    @Schema(description = "Phone number of the patient", example = "5551234567") 
    String phone

) {
    public static PatientDTO fromEntity(Patient patient) {
        return new PatientDTO(
            patient.getId(),
            patient.getFirstName(),
            patient.getLastName(),
            patient.getDateOfBirth(),
            patient.getGender(),
            patient.getAddress(),
            patient.getPhone()
        );
    }

    public Patient toEntity() {
        Patient patient = new Patient();
        patient.setId(this.id());
        patient.setFirstName(this.firstName());
        patient.setLastName(this.lastName());
        patient.setDateOfBirth(this.dateOfBirth());
        patient.setGender(this.gender());
        patient.setAddress(this.address());
        patient.setPhone(this.phone());
        return patient;
    }
}
