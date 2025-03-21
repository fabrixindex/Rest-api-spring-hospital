package com.healthflow.service;

import com.healthflow.models.Medication;
import com.healthflow.repository.MedicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MedicationService {

    private final MedicationRepository medicationRepository;

    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    public List<Medication> getAllMedications() {
        return medicationRepository.findAll();
    }

    public Optional<Medication> getMedicationById(Long id) {
        return medicationRepository.findById(id);
    }

    public Medication saveMedication(Medication medication) {
        return medicationRepository.save(medication);
    }

    public void deleteMedication(Long id) {
        medicationRepository.deleteById(id);
    }

    @Transactional
    public void reduceStock(Long id, int amount) {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Medication not found"));

        if (medication.getStock() < amount) {
            throw new IllegalArgumentException("Not enough stock for medication: " + medication.getName());
        }

        medication.setStock(medication.getStock() - amount);
        medicationRepository.save(medication);
    }

    @Transactional
    public void increaseStock(Long id, int amount) {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Medication not found"));

        medication.setStock(medication.getStock() + amount);
        medicationRepository.save(medication);
    }
}
