package com.ecocollect.repository;

import com.ecocollect.model.Collection;
import com.ecocollect.model.enums.CollectionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    List<Collection> findByContainerId(Long containerId);

    List<Collection> findByStatus(CollectionStatus status);

    List<Collection> findByContainerIdAndStatus(
            Long containerId,
            CollectionStatus status
    );

    List<Collection> findByContainerSectorId(Long sectorId);

    @Query("SELECT COALESCE(SUM(c.weightKg), 0) FROM Collection c WHERE c.status = com.ecocollect.model.enums.CollectionStatus.COMPLETED")
    Double sumCompletedWeightKg();

}
