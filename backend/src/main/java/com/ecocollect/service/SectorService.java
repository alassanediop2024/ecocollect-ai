package com.ecocollect.service;

import com.ecocollect.dto.SectorRequest;
import com.ecocollect.dto.SectorResponse;
import com.ecocollect.exception.DuplicateResourceException;
import com.ecocollect.exception.ResourceNotFoundException;
import com.ecocollect.mapper.SectorMapper;
import com.ecocollect.model.Municipality;
import com.ecocollect.model.Sector;
import com.ecocollect.repository.MunicipalityRepository;
import com.ecocollect.repository.SectorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SectorService {

    private final SectorRepository sectorRepository;
    private final MunicipalityRepository municipalityRepository;
    private final SectorMapper sectorMapper;

    public SectorService(
            SectorRepository sectorRepository,
            MunicipalityRepository municipalityRepository,
            SectorMapper sectorMapper) {

        this.sectorRepository = sectorRepository;
        this.municipalityRepository = municipalityRepository;
        this.sectorMapper = sectorMapper;
    }

    @Transactional(readOnly = true)
    public List<SectorResponse> findAll() {
        return sectorRepository.findAll()
                .stream()
                .map(sectorMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<SectorResponse> findById(Long id) {
        return sectorRepository.findById(id)
                .map(sectorMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<SectorResponse> findByMunicipality(Long municipalityId) {
        return sectorRepository.findByMunicipalityId(municipalityId)
                .stream()
                .map(sectorMapper::toResponse)
                .toList();
    }

    @Transactional
    public SectorResponse create(SectorRequest request) {

        Municipality municipality = municipalityRepository
                .findById(request.municipalityId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "La municipalité avec l'identifiant '" +
                        request.municipalityId() +
                        "' n'existe pas."
                ));

        if (sectorRepository.existsByCodeAndMunicipalityId(
                request.code(),
                request.municipalityId())) {

            throw new DuplicateResourceException(
                    "Le secteur avec le code '" +
                    request.code() +
                    "' existe déjà dans cette municipalité."
            );
        }

        Sector sector = new Sector();
        sector.setCode(request.code());
        sector.setName(request.name());
        sector.setDescription(request.description());
        sector.setMunicipality(municipality);

        Sector saved = sectorRepository.save(sector);

        return sectorMapper.toResponse(saved);
    }
}
