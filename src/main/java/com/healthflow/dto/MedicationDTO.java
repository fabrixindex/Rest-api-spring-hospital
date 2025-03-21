package com.healthflow.dto;

import com.healthflow.models.Medication;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MedicationDTO {  

    private Long id;
    private String name;
    private String dosage;
    private String description;
    private int stock;
    private LocalDate expirationDate;

    public static MedicationDTO fromEntity(Medication medication) {
        return new MedicationDTO(
            medication.getId(),
            medication.getName(),
            medication.getDosage(),
            medication.getDescription(),
            medication.getStock(),
            medication.getExpirationDate()
        );
    }

    public Medication toEntity() {
        Medication medication = new Medication();
        medication.setId(this.id);
        medication.setName(this.name);
        medication.setDosage(this.dosage);
        medication.setDescription(this.description);
        medication.setStock(this.stock);
        medication.setExpirationDate(this.expirationDate);
        return medication;
    }
}
