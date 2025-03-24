package com.healthflow.controller;

import com.healthflow.dto.PatientDTO;
import com.healthflow.models.Patient;
import com.healthflow.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/patients")
@Tag(name = "Patients", description = "API for managing patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @Operation(summary = "Get all patients", description = "Retrieves a list of all registered patients.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of patients retrieved successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        List<PatientDTO> patients = patientService.getAllPatients()
                .stream()
                .map(PatientDTO::fromEntity)
                .toList();

        return patients.isEmpty() 
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.ok(patients);
    }

    @Operation(summary = "Get patient by ID", description = "Retrieves a patient's details by their ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Patient retrieved successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDTO.class))),
        @ApiResponse(responseCode = "404", description = "Patient not found", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable Long id) {
        try {
            Patient patient = patientService.getPatientById(id);
            return ResponseEntity.ok(PatientDTO.fromEntity(patient));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: Patient with ID " + id + " not found.");
        }
    }

    @Operation(summary = "Create a new patient", description = "Registers a new patient with the given details.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Patient created successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> createPatient(@Valid @RequestBody PatientDTO patientDTO) {
        try {
            Patient patient = patientService.savePatient(patientDTO.toEntity());
            return ResponseEntity.status(HttpStatus.CREATED).body(PatientDTO.fromEntity(patient));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid request: " + e.getMessage()); 
        }
    }

    @Operation(summary = "Update a patient", description = "Updates the details of an existing patient.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Patient updated successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDTO.class))),
        @ApiResponse(responseCode = "404", description = "Patient not found", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable Long id, @Valid @RequestBody PatientDTO patientDTO) {
        try {
            Patient updatedPatient = patientService.updatePatient(id, patientDTO.toEntity());
            return ResponseEntity.ok(PatientDTO.fromEntity(updatedPatient));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Delete a patient", description = "Removes a patient from the system by their ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Patient successfully deleted", content = @Content),
        @ApiResponse(responseCode = "404", description = "Patient not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable Long id) {
        try {
            patientService.deletePatient(id);
            return ResponseEntity.ok("Patient successfully deleted.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found.");
        }
    }
    
}
