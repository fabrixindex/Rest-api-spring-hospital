package com.healthflow.controller;

import com.healthflow.dto.DoctorDTO;
import com.healthflow.models.Doctor;
import com.healthflow.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/doctors")
@Tag(name = "Doctors", description = "API for managing doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @Operation(summary = "Get all doctors", description = "Retrieves a list of all registered doctors.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of doctors retrieved successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        List<DoctorDTO> doctors = doctorService.getAllDoctors()
                .stream()
                .map(DoctorDTO::fromEntity)
                .toList();

        return doctors.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.ok(doctors);
    }

    @Operation(summary = "Get doctor by ID", description = "Retrieves a doctor's details by their ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Doctor retrieved successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorDTO.class))),
        @ApiResponse(responseCode = "404", description = "Doctor not found", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable Long id) {
        try {
            Doctor doctor = doctorService.getDoctorById(id);
            return ResponseEntity.ok(DoctorDTO.fromEntity(doctor));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: Doctor with ID " + id + " not found.");
        }
    }

    @Operation(summary = "Create a new doctor", description = "Registers a new doctor with the given details.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Doctor created successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> createDoctor(@Valid @RequestBody DoctorDTO doctorDTO) {
        try {
            Doctor doctor = doctorService.saveDoctor(doctorDTO.toEntity());
            return ResponseEntity.status(HttpStatus.CREATED).body(DoctorDTO.fromEntity(doctor));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid request: " + e.getMessage());
        }
    }

    @Operation(summary = "Update a doctor", description = "Updates the details of an existing doctor.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Doctor updated successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = DoctorDTO.class))),
        @ApiResponse(responseCode = "404", description = "Doctor not found", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<DoctorDTO> updateDoctor(@PathVariable Long id, @Valid @RequestBody DoctorDTO doctorDTO) {
        try {
            Doctor updatedDoctor = doctorService.updateDoctor(id, doctorDTO.toEntity());
            return ResponseEntity.ok(DoctorDTO.fromEntity(updatedDoctor));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Delete a doctor", description = "Removes a doctor from the system by their ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Doctor successfully deleted", content = @Content),
        @ApiResponse(responseCode = "404", description = "Doctor not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable Long id) {
        try {
            doctorService.deleteDoctor(id);
            return ResponseEntity.ok("Doctor successfully deleted.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found.");
        }
    }
}
