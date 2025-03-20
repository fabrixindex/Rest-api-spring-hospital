package com.healthflow.controller;

import com.healthflow.models.MedicalRecord;
import com.healthflow.repository.MedicalRecordRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicalRecords")
public class MedicalRecordController {

    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordController(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @GetMapping
    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecord> getMedicalRecordById(@PathVariable Long id) {
        return medicalRecordRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public MedicalRecord createMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        return medicalRecordRepository.save(medicalRecord);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@PathVariable Long id, @RequestBody MedicalRecord medicalRecordDetails) {
        return medicalRecordRepository.findById(id).map(record -> {
            record.setPatient(medicalRecordDetails.getPatient());
            record.setDiagnosis(medicalRecordDetails.getDiagnosis());
            record.setTreatment(medicalRecordDetails.getTreatment());
            record.setDate(medicalRecordDetails.getDate());
            return ResponseEntity.ok(medicalRecordRepository.save(record));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable Long id) {
        if (medicalRecordRepository.existsById(id)) {
            medicalRecordRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
