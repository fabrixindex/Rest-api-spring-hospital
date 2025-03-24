package com.healthflow.service;

import com.healthflow.models.Medication;
import com.healthflow.repository.MedicationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MedicationService {

    private final MedicationRepository medicationRepository;

    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    public List<Medication> getAllMedications() {
        return medicationRepository.findAll();
    }

    public Medication getMedicationById(Long id) {
        return medicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Medication not found with ID: " + id));
    }

    public Medication saveMedication(Medication medication) {
        validateMedication(medication);
        return medicationRepository.save(medication);
    }

    public Medication updateMedication(Long id, Medication medicationDetails) {
        Medication existingMedication = medicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Medication not found with ID: " + id));

        validateMedication(medicationDetails);

        existingMedication.setName(medicationDetails.getName());
        existingMedication.setDosage(medicationDetails.getDosage());
        existingMedication.setDescription(medicationDetails.getDescription());
        existingMedication.setStock(medicationDetails.getStock());
        existingMedication.setExpirationDate(medicationDetails.getExpirationDate());

        return medicationRepository.save(existingMedication);
    }

    public void deleteMedication(Long id) {
        if (!medicationRepository.existsById(id)) {
            throw new EntityNotFoundException("Medication not found with ID: " + id);
        }
        medicationRepository.deleteById(id);
    }

    @Transactional
    public void reduceStock(Long id, int amount) {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Medication not found with ID: " + id));

        if (amount <= 0) {
            throw new IllegalArgumentException("Reduction amount must be greater than zero.");
        }
        if (medication.getStock() < amount) {
            throw new IllegalArgumentException("Not enough stock for medication: " + medication.getName());
        }

        medication.setStock(medication.getStock() - amount);
        medicationRepository.save(medication);
    }

    @Transactional
    public void increaseStock(Long id, int amount) {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Medication not found with ID: " + id));

        if (amount <= 0) {
            throw new IllegalArgumentException("Increase amount must be greater than zero.");
        }

        medication.setStock(medication.getStock() + amount);
        medicationRepository.save(medication);
    }

    private void validateMedication(Medication medication) {
        if (medication == null) {
            throw new IllegalArgumentException("Medication object cannot be null.");
        }
        if (!StringUtils.hasText(medication.getName()) || medication.getName().length() > 100) {
            throw new IllegalArgumentException("Medication name is required and must be at most 100 characters.");
        }
        if (!StringUtils.hasText(medication.getDosage()) || medication.getDosage().length() > 50) {
            throw new IllegalArgumentException("Dosage is required and must be at most 50 characters.");
        }
        if (medication.getStock() < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
        if (medication.getExpirationDate() == null || medication.getExpirationDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Expiration date is required and must be in the future.");
        }
    }
}
