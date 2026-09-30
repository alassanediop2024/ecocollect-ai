package com.ecocollect.repository;

import com.ecocollect.model.Container;
import com.ecocollect.model.enums.ContainerStatus;
import com.ecocollect.model.enums.ContainerType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContainerRepository extends JpaRepository<Container, Long> {

    Optional<Container> findByCode(String code);

    boolean existsByCode(String code);

    long countByStatus(ContainerStatus status);

    List<Container> findBySectorId(Long sectorId);

    List<Container> findByStatus(ContainerStatus status);

    List<Container> findByContainerType(ContainerType containerType);

    List<Container> findBySectorIdAndStatus(
            Long sectorId,
            ContainerStatus status
    );
}
