package com.example.dating.domain;

import com.example.dating.dto.gwating.GwatingRoomDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class GwatingRoom {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GWATINGROOM_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROOM_MANAGER_ID")
    private Member roomManager;

    private String roomName;
    private Integer allCount;
    private Integer maleCount;
    private Integer femaleCount;
    private String roomCategory;
    private String ageCategory;
    private String location;

    public void createGwatingRoom(GwatingRoomDto gwatingRoomDto, Member member) {
        Integer allCount = gwatingRoomDto.getAllCount();

        this.roomManager = member;
        this.roomName = gwatingRoomDto.getRoomName();
        this.allCount = allCount;
        this.maleCount = allCount / 2;
        this.femaleCount = allCount / 2;
        this.roomCategory = gwatingRoomDto.getRoomCategory();
        this.ageCategory = gwatingRoomDto.getAgeCategory();
        this.location = gwatingRoomDto.getLocation();

        if (member.getGender().equals("남자")) {
            this.maleCount -= 1;
        } else {
            this.femaleCount -= 1;
        }
    }

    public void joinMale() {
        this.maleCount -= 1;
    }

    public void joinFemale() {
        this.femaleCount -= 1;
    }
}
