package com.ecocollect.mapper;

import com.ecocollect.dto.MunicipalityResponse;
import com.ecocollect.model.Municipality;
import org.springframework.stereotype.Component;

@Component
public class MunicipalityMapper {

    public MunicipalityResponse toResponse(Municipality municipality) {

        return new MunicipalityResponse(
                municipality.getId(),
                municipality.getCode(),
                municipality.getName(),
                municipality.getRegion(),
                municipality.getCreatedAt(),
                municipality.getUpdatedAt()
        );
    }
}
