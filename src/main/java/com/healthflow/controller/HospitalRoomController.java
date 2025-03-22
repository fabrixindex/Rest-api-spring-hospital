package com.healthflow.controller;

import com.healthflow.dto.HospitalRoomDTO;
import com.healthflow.models.HospitalRoom;
import com.healthflow.repository.HospitalRoomRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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

    @Operation(summary = "Get a hospital room by ID", description = "Retrieve details of a specific hospital room by its ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Hospital room found"),
        @ApiResponse(responseCode = "404", description = "Hospital room not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<HospitalRoomDTO> getHospitalRoomById(@PathVariable Long id) {
        return hospitalRoomRepository.findById(id)
                .map(HospitalRoomDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new hospital room", description = "Add a new hospital room to the system.")
    @ApiResponse(responseCode = "200", description = "Hospital room created successfully")
    @PostMapping
    public ResponseEntity<HospitalRoomDTO> createHospitalRoom(@RequestBody HospitalRoomDTO hospitalRoomDTO) {
        HospitalRoom hospitalRoom = hospitalRoomDTO.toEntity();
        HospitalRoom savedRoom = hospitalRoomRepository.save(hospitalRoom);
        return ResponseEntity.ok(HospitalRoomDTO.fromEntity(savedRoom));
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
    public ResponseEntity<String> deleteHospitalRoom(@PathVariable Long id) {
        if (hospitalRoomRepository.existsById(id)) {
            hospitalRoomRepository.deleteById(id);
            return ResponseEntity.ok("Hospital room deleted successfully.");
        }
        return ResponseEntity.notFound().build();
    }
}
