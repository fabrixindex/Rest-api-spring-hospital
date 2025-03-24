package com.healthflow.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Entity
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Dosage cannot be empty")
    @Column(nullable = false)
    private String dosage;

    @Size(max = 255, message = "Description must be at most 255 characters")
    private String description;

    @Min(value = 0, message = "Stock cannot be negative")
    private int stock;

    @NotNull(message = "Expiration date cannot be null")
    @Future(message = "Expiration date must be in the future")
    private LocalDate expirationDate;

    public Medication() {}

    public Medication(String name, String dosage, String description, int stock, LocalDate expirationDate) {
        this.name = name;
        this.dosage = dosage;
        this.description = description;
        this.stock = stock;
        this.expirationDate = expirationDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; } 

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public LocalDate getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; }

    @Override
    public String toString() {
        return "Medication{id=" + id + 
               ", name='" + name + '\'' +
               ", dosage='" + dosage + '\'' +
               ", description='" + description + '\'' +
               ", stock=" + stock +
               ", expirationDate=" + expirationDate + '}';
    }
}
