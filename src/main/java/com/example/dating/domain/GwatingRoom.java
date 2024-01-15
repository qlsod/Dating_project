package com.example.dating.domain;

import com.example.dating.dto.gwating.GwatingRoomDto;
import com.example.dating.dto.gwating.GwatingUpdateDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class GwatingRoom {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GWATINGROOM_ID")
    private Long id;

    private String image;
    private String roomName;
    private String roomDescription;
    private Integer maleCount;
    private Integer joinMaleCount = 0;
    private Integer femaleCount;
    private Integer joinFemaleCount = 0;
    private String roomCategory;
    private String location;

    public void createGwatingRoom(GwatingRoomDto gwatingRoomDto) {
        this.image = gwatingRoomDto.getImage();
        this.roomName = gwatingRoomDto.getRoomName();
        this.roomDescription = gwatingRoomDto.getRoomDescription();
        this.maleCount = gwatingRoomDto.getManCount();
        this.femaleCount = gwatingRoomDto.getWomanCount();
        this.roomCategory = gwatingRoomDto.getRoomCategory();
        this.location = gwatingRoomDto.getLocation();
    }

    public void joinMale() {
        this.joinMaleCount += 1;
    }

    public void joinFemale() {
        this.joinFemaleCount += 1;
    }

    public void updateGwatingRoom(GwatingUpdateDto gwatingUpdateDto) {
        this.roomName = gwatingUpdateDto.getRoomName();
        this.roomDescription = gwatingUpdateDto.getRoomDescription();
    }
}
