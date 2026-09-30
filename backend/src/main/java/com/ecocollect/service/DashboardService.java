package com.ecocollect.service;

import com.ecocollect.dto.DashboardStatisticsResponse;
import com.ecocollect.model.enums.AnomalySeverity;
import com.ecocollect.model.enums.AnomalyStatus;
import com.ecocollect.model.enums.ContainerStatus;
import com.ecocollect.repository.AnomalyRepository;
import com.ecocollect.repository.CollectionRepository;
import com.ecocollect.repository.ContainerRepository;
import com.ecocollect.repository.MunicipalityRepository;
import com.ecocollect.repository.SectorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final MunicipalityRepository municipalityRepository;
    private final SectorRepository sectorRepository;
    private final ContainerRepository containerRepository;
    private final CollectionRepository collectionRepository;
    private final AnomalyRepository anomalyRepository;

    public DashboardService(
            MunicipalityRepository municipalityRepository,
            SectorRepository sectorRepository,
            ContainerRepository containerRepository,
            CollectionRepository collectionRepository,
            AnomalyRepository anomalyRepository) {

        this.municipalityRepository = municipalityRepository;
        this.sectorRepository = sectorRepository;
        this.containerRepository = containerRepository;
        this.collectionRepository = collectionRepository;
        this.anomalyRepository = anomalyRepository;
    }

    public DashboardStatisticsResponse getStatistics() {

        long municipalities = municipalityRepository.count();
        long sectors = sectorRepository.count();

        long containers = containerRepository.count();
        long activeContainers =
                containerRepository.countByStatus(ContainerStatus.ACTIVE);

        long collections = collectionRepository.count();

        Double totalWeight =
                collectionRepository.sumCompletedWeightKg();

        long reportedAnomalies =
                anomalyRepository.countByStatus(AnomalyStatus.REPORTED);

        long inProgressAnomalies =
                anomalyRepository.countByStatus(AnomalyStatus.IN_PROGRESS);

        long resolvedAnomalies =
                anomalyRepository.countByStatus(AnomalyStatus.RESOLVED);

        long criticalAnomalies =
                anomalyRepository.countBySeverity(AnomalySeverity.CRITICAL);

        return new DashboardStatisticsResponse(
                municipalities,
                sectors,
                containers,
                activeContainers,
                collections,
                totalWeight != null ? totalWeight : 0.0,
                reportedAnomalies,
                inProgressAnomalies,
                resolvedAnomalies,
                criticalAnomalies
        );
    }
}
