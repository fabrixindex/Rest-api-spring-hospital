package com.healthflow.controller;

import com.healthflow.dto.DoctorDTO;
import com.healthflow.models.Doctor;
import com.healthflow.repository.DoctorRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorRepository doctorRepository;

    public DoctorController(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        List<DoctorDTO> doctors = doctorRepository.findAll().stream()
                .map(DoctorDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id) {
        return doctorRepository.findById(id)
                .map(DoctorDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DoctorDTO> createDoctor(@RequestBody DoctorDTO doctorDTO) {
        Doctor doctor = doctorDTO.toEntity();
        Doctor savedDoctor = doctorRepository.save(doctor);
        return ResponseEntity.ok(DoctorDTO.fromEntity(savedDoctor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorDTO> updateDoctor(@PathVariable Long id, @RequestBody DoctorDTO doctorDTO) {
        return doctorRepository.findById(id).map(doctor -> {
            doctor.setFirstName(doctorDTO.getFirstName());
            doctor.setLastName(doctorDTO.getLastName());
            doctor.setSpecialty(doctorDTO.getSpecialty());
            doctor.setPhone(doctorDTO.getPhone());
            doctor.setEmail(doctorDTO.getEmail());
            Doctor updatedDoctor = doctorRepository.save(doctor);
            return ResponseEntity.ok(DoctorDTO.fromEntity(updatedDoctor));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable Long id) {
        if (doctorRepository.existsById(id)) {
            doctorRepository.deleteById(id);
            return ResponseEntity.ok("Doctor deleted successfully.");
        }
        return ResponseEntity.notFound().build();
    }
}
