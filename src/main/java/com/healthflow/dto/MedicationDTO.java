package com.healthflow.dto;

import com.healthflow.models.Medication;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(description = "DTO representing a medication")
public record MedicationDTO(

    @Schema(description = "ID of the medication", example = "1")
    Long id,

    @NotBlank(message = "Name cannot be empty")
    @Size(max = 100, message = "Name must be at most 100 characters")
    @Schema(description = "Name of the medication", example = "Paracetamol")
    String name,

    @NotBlank(message = "Dosage cannot be empty")
    @Size(max = 50, message = "Dosage must be at most 50 characters")
    @Schema(description = "Dosage of the medication", example = "500mg")
    String dosage,

    @Schema(description = "Description of the medication", example = "Used for pain relief and fever reduction")
    String description,

    @Min(value = 0, message = "Stock cannot be negative")
    @Schema(description = "Stock quantity of the medication", example = "150")
    int stock,

    @NotNull(message = "Expiration date cannot be null")
    @Future(message = "Expiration date must be in the future")
    @Schema(description = "Expiration date of the medication", example = "2026-05-12")
    LocalDate expirationDate

) {
    public static MedicationDTO fromEntity(Medication medication) {
        return new MedicationDTO(
            medication.getId(),
            medication.getName(),
            medication.getDosage(),
            medication.getDescription() != null ? medication.getDescription() : "", 
            medication.getStock(),
            medication.getExpirationDate()
        );
    }

    public Medication toEntity() {
        Medication medication = new Medication();
        medication.setId(this.id());  
        medication.setName(this.name());
        medication.setDosage(this.dosage());
        medication.setDescription(this.description() != null ? this.description() : "");  
        medication.setStock(this.stock());
        medication.setExpirationDate(this.expirationDate());
        return medication;
    }
}
