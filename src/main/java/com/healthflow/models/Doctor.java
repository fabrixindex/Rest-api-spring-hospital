package com.healthflow.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String specialty;
    private String phone;
    private String email;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Appointment> appointments;
}
