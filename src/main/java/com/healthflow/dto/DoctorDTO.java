package com.healthflow.dto;

import com.healthflow.models.Doctor;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DoctorDTO {
    
    private Long id;
    private String firstName;
    private String lastName;
    private String specialty;
    private String phone;
    private String email;

    public static DoctorDTO fromEntity(Doctor doctor) {
        return new DoctorDTO(
            doctor.getId(),
            doctor.getFirstName(),
            doctor.getLastName(),
            doctor.getSpecialty(),
            doctor.getPhone(),
            doctor.getEmail()
        );
    }

    public Doctor toEntity() {
        Doctor doctor = new Doctor();
        doctor.setId(this.id);
        doctor.setFirstName(this.firstName);
        doctor.setLastName(this.lastName);
        doctor.setSpecialty(this.specialty);
        doctor.setPhone(this.phone);
        doctor.setEmail(this.email);
        return doctor;
    }
}
