package com.healthflow.controller;

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
    public List<HospitalRoom> getAllHospitalRooms() {
        return hospitalRoomRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HospitalRoom> getHospitalRoomById(@PathVariable Long id) {
        return hospitalRoomRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public HospitalRoom createHospitalRoom(@RequestBody HospitalRoom hospitalRoom) {
        return hospitalRoomRepository.save(hospitalRoom);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HospitalRoom> updateHospitalRoom(@PathVariable Long id, @RequestBody HospitalRoom hospitalRoomDetails) {
        return hospitalRoomRepository.findById(id).map(room -> {
            room.setRoomNumber(hospitalRoomDetails.getRoomNumber());
            //room.setCapacity(hospitalRoomDetails.getCapacity());
            room.setType(hospitalRoomDetails.getType());
            //room.setStatus(hospitalRoomDetails.getStatus());
            return ResponseEntity.ok(hospitalRoomRepository.save(room));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHospitalRoom(@PathVariable Long id) {
        if (hospitalRoomRepository.existsById(id)) {
            hospitalRoomRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
