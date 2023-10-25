package com.example.dating.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
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

    @Builder
    public Alert(Member receiverMember, String image, String name, String message, String sendAt) {
        this.receiverMember = receiverMember;
        this.image = image;
        this.name = name;
        this.message = message;
        this.sendAt = sendAt;
    }
}
