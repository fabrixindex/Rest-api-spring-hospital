package com.healthflow.controller;

import com.healthflow.dto.PrescriptionDTO;
import com.healthflow.service.PrescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/prescriptions")
@Tag(name = "Prescriptions", description = "API for managing prescriptions")
@Validated
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @GetMapping
    @Operation(summary = "Get all prescriptions", description = "Retrieve a list of all prescriptions")
    public ResponseEntity<List<PrescriptionDTO>> getAllPrescriptions() {
        try {
            List<PrescriptionDTO> prescriptions = prescriptionService.getAllPrescriptions();
            return ResponseEntity.ok(prescriptions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get prescription by ID", description = "Retrieve a specific prescription by its ID")
    public ResponseEntity<String> getPrescriptionById(@PathVariable Long id) {
        try {
            Optional<PrescriptionDTO> prescriptionDTO = prescriptionService.getPrescriptionById(id);
            return prescriptionDTO
                    .map(prescription -> ResponseEntity.ok("Prescription found!"))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prescription not found with ID: " + id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving prescription: " + e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "Create a new prescription", description = "Save a new prescription in the system")
    public ResponseEntity<String> createPrescription(@RequestBody PrescriptionDTO prescriptionDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation error: " + bindingResult.getAllErrors());
        }

        try {
            prescriptionService.createPrescription(prescriptionDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Prescription created successfully!");
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error creating prescription: Invalid patient, doctor, or medication ID. Please check your references.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating prescription: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing prescription", description = "Modify details of an existing prescription")
    public ResponseEntity<String> updatePrescription(@PathVariable Long id, @RequestBody PrescriptionDTO prescriptionDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation error: " + bindingResult.getAllErrors());
        }

        try {
            Optional<PrescriptionDTO> existingPrescription = prescriptionService.getPrescriptionById(id);
            if (existingPrescription.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prescription not found with ID: " + id);
            }
            prescriptionService.updatePrescription(id, prescriptionDTO);
            return ResponseEntity.ok("Prescription updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating prescription: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a prescription", description = "Remove a prescription by its ID")
    public ResponseEntity<String> deletePrescription(@PathVariable Long id) {
        try {
            Optional<PrescriptionDTO> existingPrescription = prescriptionService.getPrescriptionById(id);
            if (existingPrescription.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prescription not found with ID: " + id);
            }
            prescriptionService.deletePrescription(id);
            return ResponseEntity.ok("Prescription deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting prescription: " + e.getMessage());
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
    }
}
