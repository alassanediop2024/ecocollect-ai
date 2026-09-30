package com.ecocollect.service;

import com.ecocollect.dto.ContainerRequest;
import com.ecocollect.dto.ContainerResponse;
import com.ecocollect.mapper.ContainerMapper;
import com.ecocollect.model.Container;
import com.ecocollect.model.Sector;
import com.ecocollect.model.enums.ContainerStatus;
import com.ecocollect.repository.ContainerRepository;
import com.ecocollect.repository.SectorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ContainerService {

    private final ContainerRepository containerRepository;
    private final SectorRepository sectorRepository;
    private final ContainerMapper containerMapper;

    public ContainerService(
            ContainerRepository containerRepository,
            SectorRepository sectorRepository,
            ContainerMapper containerMapper) {

        this.containerRepository = containerRepository;
        this.sectorRepository = sectorRepository;
        this.containerMapper = containerMapper;
    }

    public List<ContainerResponse> findAll() {
        return containerRepository.findAll()
                .stream()
                .map(containerMapper::toResponse)
                .toList();
    }

    public Optional<ContainerResponse> findById(Long id) {
        return containerRepository.findById(id)
                .map(containerMapper::toResponse);
    }

    public List<ContainerResponse> findBySector(Long sectorId) {
        return containerRepository.findBySectorId(sectorId)
                .stream()
                .map(containerMapper::toResponse)
                .toList();
    }

    public List<ContainerResponse> findByStatus(ContainerStatus status) {
        return containerRepository.findByStatus(status)
                .stream()
                .map(containerMapper::toResponse)
                .toList();
    }

    @Transactional
    public ContainerResponse create(ContainerRequest request) {

        if (containerRepository.existsByCode(request.code())) {
            throw new IllegalArgumentException(
                    "Un conteneur avec le code '" +
                    request.code() +
                    "' existe déjà."
            );
        }

        Sector sector = sectorRepository
                .findById(request.sectorId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Le secteur avec l'identifiant '" +
                        request.sectorId() +
                        "' n'existe pas."
                ));

        Container container = new Container();

        container.setCode(request.code());
        container.setAddress(request.address());
        container.setLatitude(request.latitude());
        container.setLongitude(request.longitude());
        container.setContainerType(request.containerType());
        container.setCapacity(request.capacity());
        container.setSector(sector);

        if (request.status() != null) {
            container.setStatus(request.status());
        }

        Container saved = containerRepository.save(container);

        return containerMapper.toResponse(saved);
    }
}
