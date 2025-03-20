package com.healthflow.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
public class HospitalRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomNumber;
    private String type;
    private Boolean availability;

    @OneToMany(mappedBy = "hospitalRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Patient> patients;
}
