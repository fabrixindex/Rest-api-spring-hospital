package com.healthflow.controller;

import com.healthflow.dto.PrescriptionDTO;
import com.healthflow.service.PrescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @GetMapping
    public List<PrescriptionDTO> getAllPrescriptions() {
        return prescriptionService.getAllPrescriptions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionDTO> getPrescriptionById(@PathVariable Long id) {
        Optional<PrescriptionDTO> prescriptionDTO = prescriptionService.getPrescriptionById(id);
        return prescriptionDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createPrescription(@RequestBody PrescriptionDTO prescriptionDTO) {
        try {
            PrescriptionDTO savedPrescription = prescriptionService.savePrescription(prescriptionDTO);
            return ResponseEntity.ok(savedPrescription);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Error si no hay stock suficiente
        }
    }

    @PutMapping("/{id}")
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
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
        if (prescriptionService.getPrescriptionById(id).isPresent()) {
            prescriptionService.deletePrescription(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
