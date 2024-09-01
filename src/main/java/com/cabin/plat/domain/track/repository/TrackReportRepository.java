package com.cabin.plat.domain.track.repository;

import com.cabin.plat.domain.track.entity.TrackReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackReportRepository extends JpaRepository<TrackReport, Long> {
}
