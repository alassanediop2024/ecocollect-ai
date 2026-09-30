package com.ecocollect.repository;

import com.ecocollect.model.Anomaly;
import com.ecocollect.model.enums.AnomalySeverity;
import com.ecocollect.model.enums.AnomalyStatus;
import com.ecocollect.model.enums.AnomalyType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnomalyRepository extends JpaRepository<Anomaly, Long> {

    List<Anomaly> findByContainerId(Long containerId);

    List<Anomaly> findByStatus(AnomalyStatus status);

    long countByStatus(AnomalyStatus status);

    long countBySeverity(AnomalySeverity severity);

    List<Anomaly> findBySeverity(AnomalySeverity severity);

    List<Anomaly> findByType(AnomalyType type);

    List<Anomaly> findByContainerSectorId(Long sectorId);

    List<Anomaly> findByContainerIdAndStatus(
            Long containerId,
            AnomalyStatus status
    );
}
