package com.ecocollect.repository;

import com.ecocollect.model.Sector;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectorRepository extends JpaRepository<Sector, Long> {

    List<Sector> findByMunicipalityId(Long municipalityId);

    boolean existsByCodeAndMunicipalityId(
            String code,
            Long municipalityId
    );
}
