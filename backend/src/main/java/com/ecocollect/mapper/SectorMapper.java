package com.ecocollect.mapper;

import com.ecocollect.dto.SectorResponse;
import com.ecocollect.model.Sector;
import org.springframework.stereotype.Component;

@Component
public class SectorMapper {

    public SectorResponse toResponse(Sector sector) {

        return new SectorResponse(
                sector.getId(),
                sector.getCode(),
                sector.getName(),
                sector.getDescription(),
                sector.getMunicipality().getId(),
                sector.getMunicipality().getName(),
                sector.getCreatedAt(),
                sector.getUpdatedAt()
        );
    }
}
