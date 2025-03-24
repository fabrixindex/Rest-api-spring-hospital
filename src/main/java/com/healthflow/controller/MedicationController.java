package com.healthflow.controller;

import com.healthflow.dto.MedicationDTO;
import com.healthflow.models.Medication;
import com.healthflow.service.MedicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/medications")
@Tag(name = "Medications", description = "API for managing medications")
public class MedicationController {

    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @Operation(summary = "Get all medications", description = "Retrieves a list of all available medications.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of medications retrieved successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = MedicationDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<MedicationDTO>> getAllMedications() {
        List<MedicationDTO> medications = medicationService.getAllMedications()
                .stream()
                .map(MedicationDTO::fromEntity)
                .toList();
        
        return medications.isEmpty() 
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.ok(medications);
    }

    @Operation(summary = "Get medication by ID", description = "Retrieves a specific medication by its ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Medication retrieved successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = MedicationDTO.class))),
        @ApiResponse(responseCode = "404", description = "Medication not found", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getMedicationById(@PathVariable Long id) {
        try {
            Medication medication = medicationService.getMedicationById(id);
            return ResponseEntity.ok(MedicationDTO.fromEntity(medication));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: Medication with ID " + id + " not found.");
        }
    }
    

    @Operation(summary = "Create a new medication", description = "Adds a new medication to the system.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Medication created successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = MedicationDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<?> createMedication(@Valid @RequestBody MedicationDTO medicationDTO) {
        try {
            if (medicationDTO.stock() < 0) {  
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Error: Stock cannot be negative."); 
            }
            
            Medication medication = medicationService.saveMedication(medicationDTO.toEntity());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(MedicationDTO.fromEntity(medication));
    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: Could not create medication. " + e.getMessage());
        }
    }
    
    @Operation(summary = "Update a medication", description = "Updates the details of an existing medication.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Medication updated successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = MedicationDTO.class))),
        @ApiResponse(responseCode = "404", description = "Medication not found", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMedication(@PathVariable Long id, @Valid @RequestBody MedicationDTO medicationDTO) {
        try {
            Medication updatedMedication = medicationService.updateMedication(id, medicationDTO.toEntity());
            return ResponseEntity.ok(MedicationDTO.fromEntity(updatedMedication));
    
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: Medication with ID " + id + " not found.");
    
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: Could not update medication. " + e.getMessage());
        }
    }    

    @Operation(summary = "Delete a medication", description = "Removes a medication from the system.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Medication successfully deleted", content = @Content),
        @ApiResponse(responseCode = "404", description = "Medication not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMedication(@PathVariable Long id) {
        try {
            medicationService.deleteMedication(id);
            return ResponseEntity.ok("Medication successfully deleted.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medication not found.");
        }
    }
}
