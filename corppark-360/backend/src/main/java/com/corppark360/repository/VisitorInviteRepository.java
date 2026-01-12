package com.corppark360.repository;

import com.corppark360.entity.VisitorInvite;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitorInviteRepository extends JpaRepository<VisitorInvite, Long> {
  Optional<VisitorInvite> findByQrToken(String qrToken);
}
