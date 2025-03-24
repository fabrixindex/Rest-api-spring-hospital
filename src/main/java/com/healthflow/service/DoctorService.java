package com.healthflow.service;

import com.healthflow.models.Doctor;
import com.healthflow.repository.DoctorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + id));
    }

    public Doctor saveDoctor(Doctor doctor) {
        validateDoctor(doctor);
        return doctorRepository.save(doctor);
    }

    public Doctor updateDoctor(Long id, Doctor doctorDetails) {
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with ID: " + id));

        validateDoctor(doctorDetails);

        existingDoctor.setFirstName(doctorDetails.getFirstName());
        existingDoctor.setLastName(doctorDetails.getLastName());
        existingDoctor.setSpecialty(doctorDetails.getSpecialty());
        existingDoctor.setPhone(doctorDetails.getPhone());
        existingDoctor.setEmail(doctorDetails.getEmail());

        return doctorRepository.save(existingDoctor);
    }

    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new EntityNotFoundException("Doctor not found with ID: " + id);
        }
        doctorRepository.deleteById(id);
    }

    private void validateDoctor(Doctor doctor) {
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor object cannot be null");
        }
        if (!StringUtils.hasText(doctor.getFirstName()) || doctor.getFirstName().length() > 50) {
            throw new IllegalArgumentException("First name is required and must be at most 50 characters.");
        }
        if (!StringUtils.hasText(doctor.getLastName()) || doctor.getLastName().length() > 50) {
            throw new IllegalArgumentException("Last name is required and must be at most 50 characters.");
        }
        if (!StringUtils.hasText(doctor.getSpecialty()) || doctor.getSpecialty().length() > 100) {
            throw new IllegalArgumentException("Specialty is required and must be at most 100 characters.");
        }
        if (!StringUtils.hasText(doctor.getPhone()) || !doctor.getPhone().matches("\\d{10,15}")) {
            throw new IllegalArgumentException("Phone number must be between 10 and 15 digits.");
        }
        if (!StringUtils.hasText(doctor.getEmail()) || !doctor.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Valid email is required.");
        }
    }
}
