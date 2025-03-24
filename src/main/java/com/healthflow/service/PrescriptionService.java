package com.healthflow.service;

import com.healthflow.dto.PrescriptionDTO;
import com.healthflow.models.Prescription;
import com.healthflow.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    public List<PrescriptionDTO> getAllPrescriptions() {
        List<Prescription> prescriptions = prescriptionRepository.findAll();
        return prescriptions.stream()
                .map(PrescriptionDTO::fromEntity)  
                .collect(Collectors.toList());    
    }

    public Optional<PrescriptionDTO> getPrescriptionById(Long id) {
        Optional<Prescription> prescriptionOptional = prescriptionRepository.findById(id);
        if (prescriptionOptional.isPresent()) {
            return Optional.of(PrescriptionDTO.fromEntity(prescriptionOptional.get()));
        } else {
            return Optional.empty();  
        }
    }

    public PrescriptionDTO createPrescription(PrescriptionDTO prescriptionDTO) {
        Prescription prescription = prescriptionDTO.toEntity();

        Prescription savedPrescription = prescriptionRepository.save(prescription);

        return PrescriptionDTO.fromEntity(savedPrescription);
    }

    public PrescriptionDTO updatePrescription(Long id, PrescriptionDTO prescriptionDTO) {
        Optional<Prescription> prescriptionOptional = prescriptionRepository.findById(id);
        if (prescriptionOptional.isPresent()) {
            Prescription prescription = prescriptionOptional.get();

            prescription.setPatient(prescriptionDTO.patient().toEntity());
            prescription.setDoctor(prescriptionDTO.doctor().toEntity());
            prescription.setMedication(prescriptionDTO.medication().toEntity());
            prescription.setPrescriptionDate(prescriptionDTO.prescriptionDate());

            Prescription updatedPrescription = prescriptionRepository.save(prescription);

            return PrescriptionDTO.fromEntity(updatedPrescription);
        } else {
            throw new RuntimeException("Prescription not found");
        }
    }

    public void deletePrescription(Long id) {
        Optional<Prescription> prescriptionOptional = prescriptionRepository.findById(id);
        if (prescriptionOptional.isPresent()) {
            prescriptionRepository.delete(prescriptionOptional.get());
        } else {
            throw new RuntimeException("Prescription not found");
        }
    }
}
