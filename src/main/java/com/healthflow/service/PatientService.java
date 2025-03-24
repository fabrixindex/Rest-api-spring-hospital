package com.healthflow.service;

import com.healthflow.models.Patient;
import com.healthflow.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with ID: " + id));
    }

    public Patient savePatient(Patient patient) {
        validatePatient(patient);
        return patientRepository.save(patient);
    }

    public Patient updatePatient(Long id, Patient patientDetails) {
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with ID: " + id));

        validatePatient(patientDetails);

        existingPatient.setFirstName(patientDetails.getFirstName());
        existingPatient.setLastName(patientDetails.getLastName());
        existingPatient.setDateOfBirth(patientDetails.getDateOfBirth());
        existingPatient.setGender(patientDetails.getGender());
        existingPatient.setPhone(patientDetails.getPhone());

        if (StringUtils.hasText(patientDetails.getAddress())) {
            existingPatient.setAddress(patientDetails.getAddress());
        }
        if (patientDetails.getHospitalRoom() != null) {
            existingPatient.setHospitalRoom(patientDetails.getHospitalRoom());
        }

        return patientRepository.save(existingPatient);
    }

    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new EntityNotFoundException("Patient not found with ID: " + id);
        }
        patientRepository.deleteById(id);
    }
    
    private void validatePatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient object cannot be null");
        }
        if (!StringUtils.hasText(patient.getFirstName()) || patient.getFirstName().length() > 50) {
            throw new IllegalArgumentException("First name is required and must be at most 50 characters.");
        }
        if (!StringUtils.hasText(patient.getLastName()) || patient.getLastName().length() > 50) {
            throw new IllegalArgumentException("Last name is required and must be at most 50 characters.");
        }
        if (patient.getDateOfBirth() == null || patient.getDateOfBirth().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth is required and must be a valid past date.");
        }
        if (patient.getGender() == null) {
            throw new IllegalArgumentException("Gender is required.");
        }
        if (!StringUtils.hasText(patient.getPhone()) || !patient.getPhone().matches("\\d{10}")) {
            throw new IllegalArgumentException("Phone number must have exactly 10 digits.");
        }
    }
}
