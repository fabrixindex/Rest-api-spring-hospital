package com.healthflow.dto;

import com.healthflow.models.Appointment;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private LocalDateTime date;
    private String status;

    public static AppointmentDTO fromEntity(Appointment appointment) {
        return new AppointmentDTO(
            appointment.getId(),
            appointment.getPatient().getId(),
            appointment.getDoctor().getId(),
            appointment.getDate(),
            appointment.getStatus()
        );
    }

    public Appointment toEntity() {
        Appointment appointment = new Appointment();
        appointment.setId(this.id);
        appointment.setDate(this.date);
        appointment.setStatus(this.status);
        return appointment;
    }
}

