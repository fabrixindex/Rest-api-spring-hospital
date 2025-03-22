package com.healthflow.controller;

import com.healthflow.dto.MedicationDTO;
import com.healthflow.models.Medication;
import com.healthflow.service.MedicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/medications")
@Tag(name = "Medications", description = "API for managing medications")
public class MedicationController {

    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @Operation(summary = "Get all medications", description = "Retrieves a list of all available medications.")
    @ApiResponse(responseCode = "200", description = "List of medications retrieved successfully")
    @GetMapping
    public List<MedicationDTO> getAllMedications() {
        return medicationService.getAllMedications()
                .stream()
                .map(MedicationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get a medication by ID", description = "Retrieves a specific medication by its ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Medication found"),
        @ApiResponse(responseCode = "404", description = "Medication not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MedicationDTO> getMedicationById(@PathVariable Long id) {
        return medicationService.getMedicationById(id)
                .map(medication -> ResponseEntity.ok(MedicationDTO.fromEntity(medication)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new medication", description = "Adds a new medication to the system.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Medication created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input or negative stock")
    })
    @PostMapping
    public ResponseEntity<MedicationDTO> createMedication(@RequestBody MedicationDTO medicationDTO) {
        if (medicationDTO.getStock() < 0) {
            return ResponseEntity.badRequest().build();
        }
        Medication savedMedication = medicationService.saveMedication(medicationDTO.toEntity());
        return ResponseEntity.ok(MedicationDTO.fromEntity(savedMedication));
    }

    @Operation(summary = "Update an existing medication", description = "Updates the details of an existing medication.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Medication updated successfully"),
        @ApiResponse(responseCode = "404", description = "Medication not found")
    })
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

    @Operation(summary = "Delete a medication", description = "Removes a medication from the system.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Medication deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Medication not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMedication(@PathVariable Long id) {
        if (medicationService.getMedicationById(id).isPresent()) {
            medicationService.deleteMedication(id);
            return ResponseEntity.ok("Medication deleted successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medication not found.");
    }
}
