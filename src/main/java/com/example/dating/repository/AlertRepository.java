package com.example.dating.repository;

import com.example.dating.domain.Alert;
import com.example.dating.domain.Member;
import com.example.dating.dto.alert.AlertDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    @Query("select new com.example.dating.dto.alert.AlertDto(a.name, a.image, a.message, a.sendAt) from Alert a where a.receiverMember = :receiverMember")
    List<AlertDto> findByReceiverMember(Member receiverMember);
}
