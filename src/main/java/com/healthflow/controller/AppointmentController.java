package com.healthflow.controller;

import com.healthflow.dto.AppointmentDTO;
import com.healthflow.models.Appointment;
import com.healthflow.models.Doctor;
import com.healthflow.models.Patient;
import com.healthflow.repository.AppointmentRepository;
import com.healthflow.repository.DoctorRepository;
import com.healthflow.repository.PatientRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/appointments")
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

    @GetMapping
    public List<AppointmentDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(AppointmentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Long id) {
        return appointmentRepository.findById(id)
                .map(appointment -> ResponseEntity.ok(AppointmentDTO.fromEntity(appointment)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        Appointment appointment = appointmentDTO.toEntity();
        
        appointment.setDoctor(doctorRepository.findById(appointmentDTO.getDoctorId()).orElse(null));
        appointment.setPatient(patientRepository.findById(appointmentDTO.getPatientId()).orElse(null));

        if (appointment.getDoctor() == null || appointment.getPatient() == null) {
            return ResponseEntity.badRequest().build();
        }

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return ResponseEntity.ok(AppointmentDTO.fromEntity(savedAppointment));
    }

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
    

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long id) {
        if (appointmentRepository.existsById(id)) {
            appointmentRepository.deleteById(id);
            return ResponseEntity.ok("The appointment with ID " + id + " has been successfully deleted.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("No appointment found with ID" + id);
    }
}

