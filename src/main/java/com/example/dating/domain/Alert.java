package com.example.dating.domain;

import com.example.dating.dto.alert.AlertDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Alert {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ALERT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECEIVER_MEMBER_ID")
    private Member receiverMember;

    private String image;
    private String name;
    private String message;
    private String sendAt;
    private boolean isCheck;

    @Builder
    public Alert(Member receiverMember, String image, String name, String message, String sendAt, boolean isCheck) {
        this.receiverMember = receiverMember;
        this.image = image;
        this.name = name;
        this.message = message;
        this.sendAt = sendAt;
        this.isCheck = isCheck;
    }

    public void check() {
        this.isCheck = true;
    }

    public AlertDto mapEntityToDto(Alert alert) {
        return new AlertDto(alert.name, alert.image, alert.message, alert.sendAt, alert.isCheck);
    }
}
