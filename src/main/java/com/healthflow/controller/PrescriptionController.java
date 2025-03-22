package com.healthflow.controller;

import com.healthflow.dto.PrescriptionDTO;
import com.healthflow.service.PrescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/prescriptions")
@Tag(name = "Prescriptions", description = "API for managing prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @GetMapping
    @Operation(summary = "Get all prescriptions", description = "Retrieve a list of all prescriptions")
    public List<PrescriptionDTO> getAllPrescriptions() {
        return prescriptionService.getAllPrescriptions();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get prescription by ID", description = "Retrieve a specific prescription by its ID")
    public ResponseEntity<PrescriptionDTO> getPrescriptionById(@PathVariable Long id) {
        Optional<PrescriptionDTO> prescriptionDTO = prescriptionService.getPrescriptionById(id);
        return prescriptionDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new prescription", description = "Save a new prescription in the system")
    public ResponseEntity<?> createPrescription(@RequestBody PrescriptionDTO prescriptionDTO) {
        try {
            PrescriptionDTO savedPrescription = prescriptionService.savePrescription(prescriptionDTO);
            return ResponseEntity.ok(savedPrescription);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing prescription", description = "Modify details of an existing prescription")
    public ResponseEntity<?> updatePrescription(@PathVariable Long id, @RequestBody PrescriptionDTO prescriptionDTO) {
        if (!prescriptionService.getPrescriptionById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        try {
            prescriptionDTO.setId(id);
            PrescriptionDTO updatedPrescription = prescriptionService.savePrescription(prescriptionDTO);
            return ResponseEntity.ok(updatedPrescription);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a prescription", description = "Remove a prescription by its ID")
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
        if (prescriptionService.getPrescriptionById(id).isPresent()) {
            prescriptionService.deletePrescription(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
