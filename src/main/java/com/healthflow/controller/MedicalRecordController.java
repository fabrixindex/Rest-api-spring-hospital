package com.healthflow.controller;

import com.healthflow.dto.MedicalRecordDTO;
import com.healthflow.models.MedicalRecord;
import com.healthflow.repository.MedicalRecordRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordDTO> getMedicalRecordById(@PathVariable Long id) {
        return medicalRecordRepository.findById(id)
                .map(medicalRecord -> ResponseEntity.ok(convertToDTO(medicalRecord)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new medical record", description = "Saves a new medical record in the database")
    @PostMapping
    public MedicalRecord createMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        return medicalRecordRepository.save(medicalRecord);
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
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMedicalRecord(@PathVariable Long id) {
        if (medicalRecordRepository.existsById(id)) {
            medicalRecordRepository.deleteById(id);
            return ResponseEntity.ok("Medical record deleted successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Medical record not found.");
    }
}