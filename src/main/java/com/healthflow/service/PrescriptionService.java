package com.healthflow.service;

import com.healthflow.dto.PrescriptionDTO;
import com.healthflow.models.Medication;
import com.healthflow.models.Patient;
import com.healthflow.models.Prescription;
import com.healthflow.models.Doctor;
import com.healthflow.repository.PrescriptionRepository;
import com.healthflow.repository.PatientRepository;
import com.healthflow.repository.DoctorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final MedicationService medicationService;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository, 
                               MedicationService medicationService, 
                               PatientRepository patientRepository, 
                               DoctorRepository doctorRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.medicationService = medicationService;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    public List<PrescriptionDTO> getAllPrescriptions() {
        return prescriptionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<PrescriptionDTO> getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Transactional
    public PrescriptionDTO savePrescription(PrescriptionDTO prescriptionDTO) {
        Prescription prescription = convertToEntity(prescriptionDTO);

        // Validar stock antes de guardar la receta
        for (Medication medication : prescription.getMedications()) {
            Medication med = medicationService.getMedicationById(medication.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Medication not found: " + medication.getId()));
            if (med.getStock() <= 0) {
                throw new IllegalArgumentException("Not enough stock for medication: " + med.getName());
            }
        }

        // Reducir stock después de la validación
        prescription.getMedications().forEach(med -> medicationService.reduceStock(med.getId(), 1));

        Prescription savedPrescription = prescriptionRepository.save(prescription);
        return convertToDTO(savedPrescription);
    }

    @Transactional
    public void deletePrescription(Long id) {
        if (!prescriptionRepository.existsById(id)) {
            throw new IllegalArgumentException("Prescription not found with ID: " + id);
        }
        prescriptionRepository.deleteById(id);
    }

    private PrescriptionDTO convertToDTO(Prescription prescription) {
        return new PrescriptionDTO(
                prescription.getId(),
                prescription.getPatient().getId(),
                prescription.getDoctor().getId(),
                prescription.getMedications().stream().map(Medication::getId).collect(Collectors.toSet()),
                prescription.getPrescriptionDate()
        );
    }

    private Prescription convertToEntity(PrescriptionDTO prescriptionDTO) {
        Patient patient = patientRepository.findById(prescriptionDTO.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found: " + prescriptionDTO.getPatientId()));

        Doctor doctor = doctorRepository.findById(prescriptionDTO.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found: " + prescriptionDTO.getDoctorId()));

        Set<Medication> medications = prescriptionDTO.getMedicationIds().stream()
                .map(medicationService::getMedicationById)
                .map(opt -> opt.orElseThrow(() -> new IllegalArgumentException("Medication not found")))
                .collect(Collectors.toSet());

        return new Prescription(
                patient,
                doctor,
                medications,
                prescriptionDTO.getPrescriptionDate()
        );
    }
}