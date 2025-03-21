package com.healthflow.dto;

import com.healthflow.models.HospitalRoom;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HospitalRoomDTO {

    private Long id;
    private String roomNumber;
    private String type;
    private Boolean availability;

    public static HospitalRoomDTO fromEntity(HospitalRoom hospitalRoom) {
        return new HospitalRoomDTO(
                hospitalRoom.getId(),
                hospitalRoom.getRoomNumber(),
                hospitalRoom.getType(),
                hospitalRoom.getAvailability()
        );
    }

    public HospitalRoom toEntity() {
        HospitalRoom hospitalRoom = new HospitalRoom();
        hospitalRoom.setId(this.id);
        hospitalRoom.setRoomNumber(this.roomNumber);
        hospitalRoom.setType(this.type);
        hospitalRoom.setAvailability(this.availability);
        return hospitalRoom;
    }
}
