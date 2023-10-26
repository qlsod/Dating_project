package com.example.dating.repository;

import com.example.dating.domain.Alert;
import com.example.dating.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByReceiverMemberOrderByIdDesc(Member receiverMember);
}