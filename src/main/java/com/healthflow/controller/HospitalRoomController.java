package com.healthflow.controller;

import com.healthflow.dto.HospitalRoomDTO;
import com.healthflow.models.HospitalRoom;
import com.healthflow.repository.HospitalRoomRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hospitalRooms")
public class HospitalRoomController {

    private final HospitalRoomRepository hospitalRoomRepository;

    public HospitalRoomController(HospitalRoomRepository hospitalRoomRepository) {
        this.hospitalRoomRepository = hospitalRoomRepository;
    }

    @GetMapping
    public List<HospitalRoomDTO> getAllHospitalRooms() {
        return hospitalRoomRepository.findAll().stream()
                .map(HospitalRoomDTO::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HospitalRoomDTO> getHospitalRoomById(@PathVariable Long id) {
        return hospitalRoomRepository.findById(id)
                .map(HospitalRoomDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<HospitalRoomDTO> createHospitalRoom(@RequestBody HospitalRoomDTO hospitalRoomDTO) {
        HospitalRoom hospitalRoom = hospitalRoomDTO.toEntity();
        HospitalRoom savedRoom = hospitalRoomRepository.save(hospitalRoom);
        return ResponseEntity.ok(HospitalRoomDTO.fromEntity(savedRoom));
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHospitalRoom(@PathVariable Long id) {
        if (hospitalRoomRepository.existsById(id)) {
            hospitalRoomRepository.deleteById(id);
            return ResponseEntity.ok("Hospital room deleted successfully.");
        }
        return ResponseEntity.notFound().build();
    }
}
