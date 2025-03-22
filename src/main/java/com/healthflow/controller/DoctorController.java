package com.healthflow.controller;

import com.healthflow.dto.DoctorDTO;
import com.healthflow.models.Doctor;
import com.healthflow.repository.DoctorRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;

@RestController
@RequestMapping("/doctors")
@Tag(name = "Doctors", description = "API for managing doctors")
public class DoctorController {

    private final DoctorRepository doctorRepository;

    public DoctorController(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Operation(summary = "Get all doctors", description = "Retrieves a list of all registered doctors.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of doctors retrieved successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        List<DoctorDTO> doctors = doctorRepository.findAll().stream()
                .map(DoctorDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(doctors);
    }

    @Operation(summary = "Get doctor by ID", description = "Retrieves a doctor's details by their ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Doctor retrieved successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorDTO.class))),
        @ApiResponse(responseCode = "404", description = "Doctor not found", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getDoctorById(@PathVariable Long id) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if (doctor.isPresent()) {
            return ResponseEntity.ok(DoctorDTO.fromEntity(doctor.get()));
        }
        return notFoundResponse(id);
    }

    @Operation(summary = "Create a new doctor", description = "Registers a new doctor with the given details.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Doctor created successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<DoctorDTO> createDoctor(@RequestBody DoctorDTO doctorDTO) {
        Doctor doctor = doctorDTO.toEntity();
        Doctor savedDoctor = doctorRepository.save(doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(DoctorDTO.fromEntity(savedDoctor));
    }

    @Operation(summary = "Update a doctor", description = "Updates the details of an existing doctor.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Doctor updated successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorDTO.class))),
        @ApiResponse(responseCode = "404", description = "Doctor not found", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateDoctor(@PathVariable Long id, @RequestBody DoctorDTO doctorDTO) {
        Optional<Doctor> existingDoctor = doctorRepository.findById(id);
        if (existingDoctor.isPresent()) {
            Doctor doctor = existingDoctor.get();
            doctor.setFirstName(doctorDTO.getFirstName());
            doctor.setLastName(doctorDTO.getLastName());
            doctor.setSpecialty(doctorDTO.getSpecialty());
            doctor.setPhone(doctorDTO.getPhone());
            doctor.setEmail(doctorDTO.getEmail());
            Doctor updatedDoctor = doctorRepository.save(doctor);
            return ResponseEntity.ok(DoctorDTO.fromEntity(updatedDoctor));
        }
        return notFoundResponse(id);
    }

    @Operation(summary = "Delete a doctor", description = "Deletes a doctor by their ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Doctor deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Doctor not found", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDoctor(@PathVariable Long id) {
        if (doctorRepository.existsById(id)) {
            doctorRepository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Doctor deleted successfully."));
        }
        return notFoundResponse(id);
    }

    private ResponseEntity<Object> notFoundResponse(Long id) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "Doctor with ID " + id + " not found."));
    }
}
