package com.healthflow.repository;

import com.healthflow.models.HospitalRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRoomRepository extends JpaRepository<HospitalRoom, Long> {
}
