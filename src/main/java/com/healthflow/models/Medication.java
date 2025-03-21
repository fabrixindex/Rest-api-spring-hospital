package com.healthflow.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String dosage;
    private String description;
    private int stock;
    private LocalDate expirationDate;

    
    public Medication() {}

    public Medication(Long id, String name, String dosage, String description, int stock, LocalDate expirationDate) {
        this.id = id;
        this.name = name;
        this.dosage = dosage;
        this.description = description;
        setStock(stock); 
        setExpirationDate(expirationDate); 
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
    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        this.stock = stock;
    }

    public LocalDate getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDate expirationDate) {
        if (expirationDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Expiration date cannot be in the past");
        }
        this.expirationDate = expirationDate;
    }
}
