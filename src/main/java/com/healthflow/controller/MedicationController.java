package com.healthflow.controller;

import com.healthflow.dto.MedicationDTO;
import com.healthflow.models.Medication;
import com.healthflow.service.MedicationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/medications")
public class MedicationController {

    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @GetMapping
    public List<MedicationDTO> getAllMedications() {
        return medicationService.getAllMedications()
                .stream()
                .map(MedicationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicationDTO> getMedicationById(@PathVariable Long id) {
        return medicationService.getMedicationById(id)
                .map(medication -> ResponseEntity.ok(MedicationDTO.fromEntity(medication)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MedicationDTO> createMedication(@RequestBody MedicationDTO medicationDTO) {
        if (medicationDTO.getStock() < 0) {
            return ResponseEntity.badRequest().build();
        }
        Medication savedMedication = medicationService.saveMedication(medicationDTO.toEntity());
        return ResponseEntity.ok(MedicationDTO.fromEntity(savedMedication));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicationDTO> updateMedication(@PathVariable Long id, @RequestBody MedicationDTO medicationDTO) {
        return medicationService.getMedicationById(id).map(medication -> {
            medication.setName(medicationDTO.getName());
            medication.setDosage(medicationDTO.getDosage());
            medication.setDescription(medicationDTO.getDescription());
            medication.setStock(medicationDTO.getStock());
            medication.setExpirationDate(medicationDTO.getExpirationDate());

            Medication updatedMedication = medicationService.saveMedication(medication);
            return ResponseEntity.ok(MedicationDTO.fromEntity(updatedMedication));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMedication(@PathVariable Long id) {
        if (medicationService.getMedicationById(id).isPresent()) {
            medicationService.deleteMedication(id);
            return ResponseEntity.ok("Medication deleted successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medication not found.");
    }
}
