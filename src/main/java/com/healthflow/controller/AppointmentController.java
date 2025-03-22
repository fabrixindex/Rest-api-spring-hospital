package com.healthflow.controller;

import com.healthflow.dto.AppointmentDTO;
import com.healthflow.models.Appointment;
import com.healthflow.models.Doctor;
import com.healthflow.models.Patient;
import com.healthflow.repository.AppointmentRepository;
import com.healthflow.repository.DoctorRepository;
import com.healthflow.repository.PatientRepository;

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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/appointments")
@Tag(name = "Appointments", description = "API for managing appointments")
public class AppointmentController {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public AppointmentController(AppointmentRepository appointmentRepository,
                                 DoctorRepository doctorRepository,
                                 PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @Operation(summary = "Get all appointments", description = "Retrieves a list of all scheduled appointments.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of appointments retrieved successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppointmentDTO.class)))
    })
    @GetMapping
    public List<AppointmentDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(AppointmentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get appointment by ID", description = "Retrieves an appointment by its ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Appointment found", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppointmentDTO.class))),
        @ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Long id) {
        return appointmentRepository.findById(id)
                .map(appointment -> ResponseEntity.ok(AppointmentDTO.fromEntity(appointment)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new appointment", description = "Registers a new appointment with a doctor and a patient.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Appointment created successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppointmentDTO.class))),
        @ApiResponse(responseCode = "400", description = "Doctor or Patient not found")
    })
    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        Appointment appointment = appointmentDTO.toEntity();
        
        appointment.setDoctor(doctorRepository.findById(appointmentDTO.getDoctorId()).orElse(null));
        appointment.setPatient(patientRepository.findById(appointmentDTO.getPatientId()).orElse(null));

        if (appointment.getDoctor() == null || appointment.getPatient() == null) {
            return ResponseEntity.badRequest().build();
        }

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return ResponseEntity.status(HttpStatus.CREATED).body(AppointmentDTO.fromEntity(savedAppointment));
    }

    @Operation(summary = "Update an appointment", description = "Updates an existing appointment's details.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Appointment updated successfully", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppointmentDTO.class))),
        @ApiResponse(responseCode = "400", description = "Doctor or Patient not found"),
        @ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable Long id, @RequestBody AppointmentDTO appointmentDTO) {
        return appointmentRepository.findById(id).map(appointment -> {
            
            Doctor doctor = doctorRepository.findById(appointmentDTO.getDoctorId()).orElse(null);
            Patient patient = patientRepository.findById(appointmentDTO.getPatientId()).orElse(null);
    
            if (doctor == null || patient == null) {
                return ResponseEntity.badRequest().body("Doctor or Patient not found"); 
            }
    
            appointment.setDoctor(doctor);
            appointment.setPatient(patient);
            appointment.setDate(appointmentDTO.getDate());
            appointment.setStatus(appointmentDTO.getStatus());
    
            Appointment updatedAppointment = appointmentRepository.save(appointment);
    
            return ResponseEntity.ok(AppointmentDTO.fromEntity(updatedAppointment));
        }).orElse(ResponseEntity.notFound().build());
    }    

    @Operation(summary = "Delete an appointment", description = "Deletes an appointment by its ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Appointment deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long id) {
        if (appointmentRepository.existsById(id)) {
            appointmentRepository.deleteById(id);
            return ResponseEntity.ok("The appointment with ID " + id + " has been successfully deleted.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("No appointment found with ID " + id);
    }
}
