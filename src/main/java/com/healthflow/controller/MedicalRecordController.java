package com.healthflow.controller;

import com.healthflow.dto.MedicalRecordDTO;
import com.healthflow.models.MedicalRecord;
import com.healthflow.repository.MedicalRecordRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/medicalRecords")
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

    @GetMapping
    public List<MedicalRecordDTO> getAllMedicalRecords() {
        List<MedicalRecord> medicalRecords = medicalRecordRepository.findAll();
        return medicalRecords.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordDTO> getMedicalRecordById(@PathVariable Long id) {
        return medicalRecordRepository.findById(id)
                .map(medicalRecord -> ResponseEntity.ok(convertToDTO(medicalRecord)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public MedicalRecord createMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        return medicalRecordRepository.save(medicalRecord);
    }

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
