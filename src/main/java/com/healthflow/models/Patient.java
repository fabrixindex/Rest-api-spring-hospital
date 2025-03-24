package com.healthflow.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString(exclude = "medicalHistory")
@EqualsAndHashCode(exclude = "medicalHistory")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50, message = "First name must be at most 50 characters")
    @Column(nullable = false)
    private String firstName;

    @Size(max = 50, message = "Last name must be at most 50 characters")
    @Column(nullable = false)
    private String lastName;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @NotBlank(message = "Gender cannot be empty")
    @Pattern(regexp = "Male|Female|Other", message = "Invalid gender")
    @Column(nullable = false, length = 10)
    private String gender;

    @Size(max = 255, message = "Address must be at most 255 characters")
    private String address;

    @Pattern(regexp = "\\d{10,15}", message = "Phone number must be between 10 and 15 digits")
    private String phone;

    @ManyToOne
    @JoinColumn(name = "hospital_room_id")
    private HospitalRoom hospitalRoom;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("date DESC")
    @JsonIgnore
    private List<MedicalRecord> medicalHistory;
}
