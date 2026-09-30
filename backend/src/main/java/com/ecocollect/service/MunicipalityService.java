package com.ecocollect.service;

import com.ecocollect.dto.MunicipalityRequest;
import com.ecocollect.dto.MunicipalityResponse;
import com.ecocollect.mapper.MunicipalityMapper;
import com.ecocollect.model.Municipality;
import com.ecocollect.repository.MunicipalityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MunicipalityService {

    private final MunicipalityRepository municipalityRepository;
    private final MunicipalityMapper municipalityMapper;

    public MunicipalityService(
            MunicipalityRepository municipalityRepository,
            MunicipalityMapper municipalityMapper) {

        this.municipalityRepository = municipalityRepository;
        this.municipalityMapper = municipalityMapper;
    }

    public List<MunicipalityResponse> findAll() {
        return municipalityRepository.findAll()
                .stream()
                .map(municipalityMapper::toResponse)
                .toList();
    }

    public Optional<MunicipalityResponse> findById(Long id) {
        return municipalityRepository.findById(id)
                .map(municipalityMapper::toResponse);
    }

    public Optional<MunicipalityResponse> findByCode(String code) {
        return municipalityRepository.findByCode(code)
                .map(municipalityMapper::toResponse);
    }

    public MunicipalityResponse create(MunicipalityRequest request) {

        if (municipalityRepository.existsByCode(request.code())) {
            throw new IllegalArgumentException(
                    "Une municipalité avec le code '" +
                    request.code() +
                    "' existe déjà."
            );
        }

        Municipality municipality = new Municipality();
        municipality.setCode(request.code());
        municipality.setName(request.name());
        municipality.setRegion(request.region());

        Municipality saved = municipalityRepository.save(municipality);

        return municipalityMapper.toResponse(saved);
    }
}
