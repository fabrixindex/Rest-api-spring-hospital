/*package com.healthflow.config;

import com.healthflow.models.*;
import com.healthflow.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            AppointmentRepository appointmentRepository,
            MedicalRecordRepository medicalRecordRepository,
            PrescriptionRepository prescriptionRepository,
            MedicationRepository medicationRepository,
            HospitalRoomRepository hospitalRoomRepository
    ) {
        return args -> {
            // Crear habitación hospitalaria
            HospitalRoom room = new HospitalRoom(null, "101", "General", true, new ArrayList<>());
            hospitalRoomRepository.save(room); // Guardar antes de asignar a los pacientes

            // Crear pacientes y asignarles la habitación
            Patient patient1 = new Patient(null, "Juan", "Pérez", LocalDate.of(1990, 5, 10), "Male", "Av. Siempre Viva 123", "123456789", room, new ArrayList<>());
            Patient patient2 = new Patient(null, "María", "Gómez", LocalDate.of(1985, 8, 22), "Female", "Calle Falsa 456", "987654321", room, new ArrayList<>());

            // Asociar pacientes a la habitación
            room.setPatients(List.of(patient1, patient2));
            patientRepository.saveAll(List.of(patient1, patient2));

            // Crear médicos
            Doctor doctor1 = new Doctor(null, "Carlos", "Fernández", "Cardiology", "1122334455", "carlos@healthflow.com", new ArrayList<>());
            Doctor doctor2 = new Doctor(null, "Lucía", "Martínez", "Pediatrics", "5566778899", "lucia@healthflow.com", new ArrayList<>());
            doctorRepository.saveAll(List.of(doctor1, doctor2));

            // Crear citas médicas
            Appointment appointment1 = new Appointment(null, patient1, doctor1, LocalDateTime.now().plusDays(1), "Scheduled");
            Appointment appointment2 = new Appointment(null, patient2, doctor2, LocalDateTime.now().plusDays(3), "Scheduled");
            appointmentRepository.saveAll(List.of(appointment1, appointment2));

            // Crear historial médico
            MedicalRecord record1 = new MedicalRecord(null, patient1, "Hipertensión", "Tratamiento con enalapril", LocalDate.now().minusMonths(2));
            MedicalRecord record2 = new MedicalRecord(null, patient2, "Gripe", "Reposo y paracetamol", LocalDate.now().minusWeeks(1));
            medicalRecordRepository.saveAll(List.of(record1, record2));

            // Crear medicamentos
            Medication med1 = new Medication(null, "Paracetamol", "500mg cada 8 horas", "Analgésico y antipirético");
            Medication med2 = new Medication(null, "Enalapril", "10mg una vez al día", "Antihipertensivo");
            medicationRepository.saveAll(List.of(med1, med2));

            // Crear recetas médicas
            Prescription prescription1 = new Prescription(null, patient1, doctor1, List.of(med2), LocalDate.now());
            Prescription prescription2 = new Prescription(null, patient2, doctor2, List.of(med1), LocalDate.now());
            prescriptionRepository.saveAll(List.of(prescription1, prescription2));

            System.out.println("✅ Datos de prueba insertados correctamente.");
        };
    }
}*/
