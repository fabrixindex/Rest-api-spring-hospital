package com.healthflow.controller;

import com.healthflow.dto.PatientDTO;
import com.healthflow.models.Patient;
import com.healthflow.repository.PatientRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@Tag(name = "Patients", description = "API for managing patients")
public class PatientController {

    private final PatientRepository patientRepository;

    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Operation(summary = "Get all patients", description = "Retrieves a list of all registered patients.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of patients retrieved successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDTO.class)))
    })
    @GetMapping
    public ResponseEntity<?> getAllPatients() {
        List<PatientDTO> patients = patientRepository.findAll().stream()
                .map(PatientDTO::fromEntity)
                .toList();
        
        if (patients.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No patients found.");
        }

        return ResponseEntity.ok(patients);
    }

    @Operation(summary = "Get patient by ID", description = "Retrieves a patient's details by their ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Patient retrieved successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDTO.class))),
        @ApiResponse(responseCode = "404", description = "Patient not found", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable Long id) {
        return patientRepository.findById(id)
                .map(PatientDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    

    @Operation(summary = "Create a new patient", description = "Registers a new patient with the given details.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Patient created successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<String> createPatient(@RequestBody PatientDTO patientDTO) {
        Patient patient = patientDTO.toEntity();
        Patient savedPatient = patientRepository.save(patient);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Patient created successfully with ID: " + savedPatient.getId());
    }

    @Operation(summary = "Update a patient", description = "Updates the details of an existing patient.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Patient updated successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDTO.class))),
        @ApiResponse(responseCode = "404", description = "Patient not found", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable Long id, @RequestBody PatientDTO patientDTO) {
        return patientRepository.findById(id).map(patient -> {
            patient.setFirstName(patientDTO.firstName());
            patient.setLastName(patientDTO.lastName());
            patient.setDateOfBirth(patientDTO.dateOfBirth());
            Patient updatedPatient = patientRepository.save(patient);
            return ResponseEntity.ok(PatientDTO.fromEntity(updatedPatient));
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build()); 
    }

    @Operation(summary = "Delete a patient", description = "Removes a patient from the system by their ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Patient successfully deleted", content = @Content),
        @ApiResponse(responseCode = "404", description = "Patient not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable Long id) {
        return patientRepository.findById(id).map(patient -> {
            patientRepository.delete(patient);
            return ResponseEntity.ok("Patient successfully deleted.");
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found."));
    }
}