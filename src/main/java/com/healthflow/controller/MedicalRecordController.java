package com.healthflow.controller;

import com.healthflow.dto.MedicalRecordDTO;
import com.healthflow.models.MedicalRecord;
import com.healthflow.repository.MedicalRecordRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/medicalRecords")
@Tag(name = "Medical Records", description = "API for managing medical records")
public class MedicalRecordController {

    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordController(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    private MedicalRecordDTO convertToDTO(MedicalRecord medicalRecord) {
        return new MedicalRecordDTO(
                medicalRecord.getId(),
                medicalRecord.getPatient().getId(),  
                medicalRecord.getDiagnosis(),
                medicalRecord.getTreatment(),
                medicalRecord.getDate()
        );
    }

    @Operation(summary = "Get all medical records", description = "Retrieves a list of all medical records")
    @GetMapping
    public List<MedicalRecordDTO> getAllMedicalRecords() {
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findAll();
        return medicalRecords.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get a medical record by ID", description = "Retrieves a specific medical record by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Medical record retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MedicalRecordDTO.class))),
        @ApiResponse(responseCode = "404", description = "Medical record not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getMedicalRecordById(@PathVariable Long id) {
        try {
            MedicalRecord medicalRecord = medicalRecordRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Medical record with ID " + id + " not found."));
            return ResponseEntity.ok(convertToDTO(medicalRecord));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }

    @Operation(summary = "Create a new medical record", description = "Saves a new medical record in the database")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Medical record created successfully",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = MedicalRecordDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> createMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        try {
            MedicalRecord savedRecord = medicalRecordRepository.save(medicalRecord);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedRecord));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: Unable to create medical record. " + e.getMessage());
        }
    }

    @Operation(summary = "Update a medical record", description = "Updates an existing medical record by its ID")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateMedicalRecord(@PathVariable Long id, @RequestBody MedicalRecord medicalRecordDetails) {
        return medicalRecordRepository.findById(id).map(record -> {
            record.setPatient(medicalRecordDetails.getPatient());
            record.setDiagnosis(medicalRecordDetails.getDiagnosis());
            record.setTreatment(medicalRecordDetails.getTreatment());
            record.setDate(medicalRecordDetails.getDate());
            medicalRecordRepository.save(record);
            return ResponseEntity.ok("Medical record updated successfully.");
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Medical record not found."));
    }

    @Operation(summary = "Delete a medical record", description = "Deletes a medical record by its ID")
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Medical record deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Medical record not found")
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<?> deleteMedicalRecord(@PathVariable Long id) {
            try {
                if (medicalRecordRepository.existsById(id)) {
                    medicalRecordRepository.deleteById(id);
                    return ResponseEntity.ok("Medical record deleted successfully.");
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Error: Medical record with ID " + id + " not found.");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error: An unexpected error occurred. " + e.getMessage());
            }
        }
}