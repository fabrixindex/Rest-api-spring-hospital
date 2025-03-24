package com.healthflow.dto;

import com.healthflow.models.Doctor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "DTO representing a doctor")
public record DoctorDTO(

    @Schema(description = "ID of the doctor", example = "1") 
    Long id,

    @NotBlank(message = "First name cannot be empty")
    @Size(max = 50, message = "First name must be at most 50 characters")
    @Schema(description = "First name of the doctor", example = "Alice") 
    String firstName,

    @NotBlank(message = "Last name cannot be empty")
    @Size(max = 50, message = "Last name must be at most 50 characters")
    @Schema(description = "Last name of the doctor", example = "Smith") 
    String lastName,

    @NotBlank(message = "Specialty cannot be empty")
    @Size(max = 100, message = "Specialty must be at most 100 characters")
    @Schema(description = "Specialty of the doctor", example = "Cardiology") 
    String specialty,

    @Pattern(regexp = "\\d{10,15}", message = "Phone number must be between 10 and 15 digits")
    @Schema(description = "Phone number of the doctor", example = "5559876543") 
    String phone,

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @Schema(description = "Email of the doctor", example = "alice.smith@example.com") 
    String email

) {
    public static DoctorDTO fromEntity(Doctor doctor) {
        return new DoctorDTO(
            doctor.getId(),
            doctor.getFirstName(),
            doctor.getLastName(),
            doctor.getSpecialty(),
            doctor.getPhone(),
            doctor.getEmail()
        );
    }

    public Doctor toEntity() {
        Doctor doctor = new Doctor();
        doctor.setId(this.id());
        doctor.setFirstName(this.firstName());
        doctor.setLastName(this.lastName());
        doctor.setSpecialty(this.specialty());
        doctor.setPhone(this.phone());
        doctor.setEmail(this.email());
        return doctor;
    }
}
