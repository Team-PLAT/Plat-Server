package com.cabin.plat.domain.track.repository;

import com.cabin.plat.domain.track.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
