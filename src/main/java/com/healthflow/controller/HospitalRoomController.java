package com.healthflow.controller;

import com.healthflow.dto.HospitalRoomDTO;
import com.healthflow.models.HospitalRoom;
import com.healthflow.repository.HospitalRoomRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hospitalRooms")
@Tag(name = "Hospital Rooms", description = "API for managing hospital rooms")
public class HospitalRoomController {

    private final HospitalRoomRepository hospitalRoomRepository;

    public HospitalRoomController(HospitalRoomRepository hospitalRoomRepository) {
        this.hospitalRoomRepository = hospitalRoomRepository;
    }

    @Operation(summary = "Get all hospital rooms", description = "Retrieve a list of all hospital rooms available.")
    @ApiResponse(responseCode = "200", description = "List of hospital rooms retrieved successfully")
    @GetMapping
    public List<HospitalRoomDTO> getAllHospitalRooms() {
        return hospitalRoomRepository.findAll().stream()
                .map(HospitalRoomDTO::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHospitalRoomById(@PathVariable Long id) {
        try {
            HospitalRoom hospitalRoom = hospitalRoomRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Hospital room with ID " + id + " not found."));
            return ResponseEntity.ok(HospitalRoomDTO.fromEntity(hospitalRoom));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }

    @Operation(summary = "Create a new hospital room", description = "Add a new hospital room to the system.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Hospital room created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    public ResponseEntity<?> createHospitalRoom(@RequestBody HospitalRoomDTO hospitalRoomDTO) {
        try {
            HospitalRoom hospitalRoom = hospitalRoomDTO.toEntity();
            HospitalRoom savedRoom = hospitalRoomRepository.save(hospitalRoom);
            return ResponseEntity.status(HttpStatus.CREATED).body(HospitalRoomDTO.fromEntity(savedRoom));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: Data integrity violation. Please check your input.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid request: " + e.getMessage());
        }
    }

    @Operation(summary = "Update a hospital room", description = "Modify the details of an existing hospital room.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Hospital room updated successfully"),
        @ApiResponse(responseCode = "404", description = "Hospital room not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<HospitalRoomDTO> updateHospitalRoom(@PathVariable Long id, @RequestBody HospitalRoomDTO hospitalRoomDTO) {
        return hospitalRoomRepository.findById(id).map(room -> {
            room.setRoomNumber(hospitalRoomDTO.getRoomNumber());
            room.setType(hospitalRoomDTO.getType());
            room.setAvailability(hospitalRoomDTO.getAvailability());

            HospitalRoom updatedRoom = hospitalRoomRepository.save(room);
            return ResponseEntity.ok(HospitalRoomDTO.fromEntity(updatedRoom));
        }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a hospital room", description = "Remove a hospital room from the system by its ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Hospital room deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Hospital room not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHospitalRoom(@PathVariable Long id) {
        if (hospitalRoomRepository.existsById(id)) {
            hospitalRoomRepository.deleteById(id);
            return ResponseEntity.ok("Hospital room deleted successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Error: Hospital room with ID " + id + " not found.");
    }
}
