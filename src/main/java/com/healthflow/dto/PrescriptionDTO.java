package com.healthflow.dto;

import com.healthflow.models.Prescription;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "DTO representing a prescription")
public record PrescriptionDTO(

    @Schema(description = "ID of the prescription", example = "1")
    Long id,

    @NotNull(message = "Patient ID cannot be null")
    @Schema(description = "Patient details", example = "{ \"id\": 10, \"firstName\": \"John\", \"lastName\": \"Doe\" }")
    PatientDTO patient,  

    @NotNull(message = "Doctor ID cannot be null")
    @Schema(description = "Doctor details", example = "{ \"id\": 5, \"name\": \"Dr. Smith\" }")
    DoctorDTO doctor,  

    @NotNull(message = "Medication ID cannot be null")
    @Schema(description = "Medication details", example = "{ \"id\": 1, \"name\": \"Aspirin\", \"dosage\": \"500mg\" }")
    MedicationDTO medication,  

    @NotNull(message = "Prescription date cannot be null")
    @Schema(description = "Date of the prescription", example = "2024-06-15")
    LocalDate prescriptionDate

) {

    public static PrescriptionDTO fromEntity(Prescription prescription) {
        if (prescription == null) {
            throw new IllegalArgumentException("Prescription cannot be null");
        }

        PatientDTO patientDTO = PatientDTO.fromEntity(prescription.getPatient());
        DoctorDTO doctorDTO = DoctorDTO.fromEntity(prescription.getDoctor());
        MedicationDTO medicationDTO = MedicationDTO.fromEntity(prescription.getMedication());

        return new PrescriptionDTO(
            prescription.getId(),
            patientDTO,
            doctorDTO,
            medicationDTO,
            prescription.getPrescriptionDate()
        );
    }

    public Prescription toEntity() {
        if (this.patient() == null || this.doctor() == null || this.medication() == null) {
            throw new IllegalArgumentException("Patient, Doctor, and Medication cannot be null");
        }
        return new Prescription(
            this.patient().toEntity(), 
            this.doctor().toEntity(), 
            this.medication().toEntity(), 
            this.prescriptionDate()
        );
    }
}
