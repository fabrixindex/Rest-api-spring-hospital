package com.healthflow.controller;

import com.healthflow.models.Medication;
import com.healthflow.repository.MedicationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medications")
public class MedicationController {

    private final MedicationRepository medicationRepository;

    public MedicationController(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    @GetMapping
    public List<Medication> getAllMedications() {
        return medicationRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medication> getMedicationById(@PathVariable Long id) {
        return medicationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Medication createMedication(@RequestBody Medication medication) {
        return medicationRepository.save(medication);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medication> updateMedication(@PathVariable Long id, @RequestBody Medication medicationDetails) {
        return medicationRepository.findById(id).map(medication -> {
            medication.setName(medicationDetails.getName());
            medication.setDosage(medicationDetails.getDosage());
            medication.setDescription(medicationDetails.getDescription());
            return ResponseEntity.ok(medicationRepository.save(medication));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable Long id) {
        if (medicationRepository.existsById(id)) {
            medicationRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
