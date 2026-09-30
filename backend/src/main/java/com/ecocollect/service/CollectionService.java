package com.ecocollect.service;

import com.ecocollect.dto.CollectionRequest;
import com.ecocollect.dto.CollectionResponse;
import com.ecocollect.mapper.CollectionMapper;
import com.ecocollect.model.Collection;
import com.ecocollect.model.Container;
import com.ecocollect.model.enums.CollectionStatus;
import com.ecocollect.repository.CollectionRepository;
import com.ecocollect.repository.ContainerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final ContainerRepository containerRepository;
    private final CollectionMapper collectionMapper;

    public CollectionService(
            CollectionRepository collectionRepository,
            ContainerRepository containerRepository,
            CollectionMapper collectionMapper) {

        this.collectionRepository = collectionRepository;
        this.containerRepository = containerRepository;
        this.collectionMapper = collectionMapper;
    }

    public List<CollectionResponse> findAll() {
        return collectionRepository.findAll()
                .stream()
                .map(collectionMapper::toResponse)
                .toList();
    }

    public Optional<CollectionResponse> findById(Long id) {
        return collectionRepository.findById(id)
                .map(collectionMapper::toResponse);
    }

    public List<CollectionResponse> findByContainer(Long containerId) {
        return collectionRepository.findByContainerId(containerId)
                .stream()
                .map(collectionMapper::toResponse)
                .toList();
    }

    public List<CollectionResponse> findBySector(Long sectorId) {
        return collectionRepository.findByContainerSectorId(sectorId)
                .stream()
                .map(collectionMapper::toResponse)
                .toList();
    }

    public List<CollectionResponse> findByStatus(CollectionStatus status) {
        return collectionRepository.findByStatus(status)
                .stream()
                .map(collectionMapper::toResponse)
                .toList();
    }

    @Transactional
    public CollectionResponse create(CollectionRequest request) {

        Container container = containerRepository
                .findById(request.containerId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Le conteneur avec l'identifiant '" +
                        request.containerId() +
                        "' n'existe pas."
                ));

        Collection collection = new Collection();

        collection.setContainer(container);
        collection.setCollectionDate(request.collectionDate());
        collection.setWeightKg(request.weightKg());
        collection.setOperator(request.operator());
        collection.setNotes(request.notes());

        if (request.status() != null) {
            collection.setStatus(request.status());
        }

        Collection saved = collectionRepository.save(collection);

        return collectionMapper.toResponse(saved);
    }
}
