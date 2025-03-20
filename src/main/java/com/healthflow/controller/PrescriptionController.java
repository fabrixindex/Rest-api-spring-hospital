package com.healthflow.controller;

import com.healthflow.models.Prescription;
import com.healthflow.repository.PrescriptionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {

    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionController(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    @GetMapping
    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prescription> getPrescriptionById(@PathVariable Long id) {
        return prescriptionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Prescription createPrescription(@RequestBody Prescription prescription) {
        return prescriptionRepository.save(prescription);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Prescription> updatePrescription(@PathVariable Long id, @RequestBody Prescription prescriptionDetails) {
        return prescriptionRepository.findById(id).map(prescription -> {
            prescription.setPatient(prescriptionDetails.getPatient());
            prescription.setDoctor(prescriptionDetails.getDoctor());
            prescription.setMedications(prescriptionDetails.getMedications());
            prescription.setDate(prescriptionDetails.getDate());
            return ResponseEntity.ok(prescriptionRepository.save(prescription));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
        if (prescriptionRepository.existsById(id)) {
            prescriptionRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
