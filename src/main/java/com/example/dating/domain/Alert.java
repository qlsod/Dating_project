package com.example.dating.domain;

import com.example.dating.dto.alert.AlertDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SENDER_MEMBER_ID")
    private Member senderMember;

    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd-HH:mm:ss", timezone = "Asia/Seoul")
    @Column(name = "created_at")
    private LocalDateTime createdAt; // 보낸 시간

    private boolean chatExist;

//    @Builder
//    public Alert(Member receiverMember, Member senderMember, String content, LocalDateTime createdAt, boolean chatExist) {
//        this.receiverMember = receiverMember;
//        this.senderMember = senderMember;
//        this.content = content;
//        this.createdAt = createdAt;
//        this.chatExist = chatExist;
//    }
//
//    public void check() {
//        this.chatExist = true;
//    }

//    public AlertDto mapEntityToDto(Alert alert) {
//        return new AlertDto(alert.senderMember, alert.content, alert.sendAt, alert.chatExist);
//    }
}
