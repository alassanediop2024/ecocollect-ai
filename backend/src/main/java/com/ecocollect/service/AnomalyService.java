package com.ecocollect.service;

import com.ecocollect.dto.AnomalyRequest;
import com.ecocollect.dto.AnomalyResponse;
import com.ecocollect.exception.ResourceNotFoundException;
import com.ecocollect.mapper.AnomalyMapper;
import com.ecocollect.model.Anomaly;
import com.ecocollect.model.Container;
import com.ecocollect.model.enums.AnomalySeverity;
import com.ecocollect.model.enums.AnomalyStatus;
import com.ecocollect.model.enums.AnomalyType;
import com.ecocollect.repository.AnomalyRepository;
import com.ecocollect.repository.ContainerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AnomalyService {

    private final AnomalyRepository anomalyRepository;
    private final ContainerRepository containerRepository;
    private final AnomalyMapper anomalyMapper;

    public AnomalyService(
            AnomalyRepository anomalyRepository,
            ContainerRepository containerRepository,
            AnomalyMapper anomalyMapper) {

        this.anomalyRepository = anomalyRepository;
        this.containerRepository = containerRepository;
        this.anomalyMapper = anomalyMapper;
    }

    public List<AnomalyResponse> findAll() {
        return anomalyRepository.findAll()
                .stream()
                .map(anomalyMapper::toResponse)
                .toList();
    }

    public Optional<AnomalyResponse> findById(Long id) {
        return anomalyRepository.findById(id)
                .map(anomalyMapper::toResponse);
    }

    public List<AnomalyResponse> findByContainer(Long containerId) {
        return anomalyRepository.findByContainerId(containerId)
                .stream()
                .map(anomalyMapper::toResponse)
                .toList();
    }

    public List<AnomalyResponse> findBySector(Long sectorId) {
        return anomalyRepository.findByContainerSectorId(sectorId)
                .stream()
                .map(anomalyMapper::toResponse)
                .toList();
    }

    public List<AnomalyResponse> findByStatus(AnomalyStatus status) {
        return anomalyRepository.findByStatus(status)
                .stream()
                .map(anomalyMapper::toResponse)
                .toList();
    }

    public List<AnomalyResponse> findBySeverity(AnomalySeverity severity) {
        return anomalyRepository.findBySeverity(severity)
                .stream()
                .map(anomalyMapper::toResponse)
                .toList();
    }

    public List<AnomalyResponse> findByType(AnomalyType type) {
        return anomalyRepository.findByType(type)
                .stream()
                .map(anomalyMapper::toResponse)
                .toList();
    }

    @Transactional
    public AnomalyResponse create(AnomalyRequest request) {

        Container container = containerRepository
                .findById(request.containerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Le conteneur avec l'identifiant '" +
                        request.containerId() +
                        "' n'existe pas."
                ));

        Anomaly anomaly = new Anomaly();

        anomaly.setContainer(container);
        anomaly.setType(request.type());
        anomaly.setDescription(request.description());
        anomaly.setSeverity(request.severity());

        if (request.status() != null) {
            anomaly.setStatus(request.status());
        }

        if (request.reportedAt() != null) {
            anomaly.setReportedAt(request.reportedAt());
        }

        Anomaly saved = anomalyRepository.save(anomaly);

        return anomalyMapper.toResponse(saved);
    }

    @Transactional
    public Optional<AnomalyResponse> resolve(Long id) {

        return anomalyRepository.findById(id)
                .map(anomaly -> {
                    anomaly.setStatus(AnomalyStatus.RESOLVED);
                    anomaly.setResolvedAt(LocalDateTime.now());

                    Anomaly saved = anomalyRepository.saveAndFlush(anomaly);

                    return anomalyMapper.toResponse(saved);
                });
    }
}
