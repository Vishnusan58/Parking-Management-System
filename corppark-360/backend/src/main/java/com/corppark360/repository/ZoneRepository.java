package com.corppark360.repository;

import com.corppark360.entity.AccessLevel;
import com.corppark360.entity.Zone;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneRepository extends JpaRepository<Zone, Integer> {
  Optional<Zone> findByAccessLevel(AccessLevel accessLevel);

  List<Zone> findByAccessLevelIn(List<AccessLevel> accessLevels);
}
