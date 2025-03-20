package com.healthflow.controller;

import com.healthflow.dto.PatientDTO;
import com.healthflow.models.Patient;
import com.healthflow.repository.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientRepository patientRepository;

    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        List<PatientDTO> patients = patientRepository.findAll().stream()
                .map(PatientDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable Long id) {
        return patientRepository.findById(id)
                .map(PatientDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<PatientDTO> createPatient(@RequestBody PatientDTO patientDTO) {
        Patient patient = patientDTO.toEntity();
        Patient savedPatient = patientRepository.save(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(PatientDTO.fromEntity(savedPatient));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable Long id, @RequestBody PatientDTO patientDTO) {
        return patientRepository.findById(id).map(patient -> {
            patient.setFirstName(patientDTO.firstName());
            patient.setLastName(patientDTO.lastName());
            patient.setDateOfBirth(patientDTO.dateOfBirth());
            Patient updatedPatient = patientRepository.save(patient);
            return ResponseEntity.ok(PatientDTO.fromEntity(updatedPatient));
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}