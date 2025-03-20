package com.healthflow.service;

import com.healthflow.models.HospitalRoom;
import com.healthflow.repository.HospitalRoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HospitalRoomService {

    private final HospitalRoomRepository hospitalRoomRepository;

    public HospitalRoomService(HospitalRoomRepository hospitalRoomRepository) {
        this.hospitalRoomRepository = hospitalRoomRepository;
    }

    public List<HospitalRoom> getAllRooms() {
        return hospitalRoomRepository.findAll();
    }

    public Optional<HospitalRoom> getRoomById(Long id) {
        return hospitalRoomRepository.findById(id);
    }

    public HospitalRoom saveRoom(HospitalRoom hospitalRoom) {
        return hospitalRoomRepository.save(hospitalRoom);
    }

    public void deleteRoom(Long id) {
        hospitalRoomRepository.deleteById(id);
    }
}
