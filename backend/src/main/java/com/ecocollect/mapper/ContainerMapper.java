package com.ecocollect.mapper;

import com.ecocollect.dto.ContainerResponse;
import com.ecocollect.model.Container;
import org.springframework.stereotype.Component;

@Component
public class ContainerMapper {

    public ContainerResponse toResponse(Container container) {
        return new ContainerResponse(
            container.getId(),
            container.getCode(),
            container.getAddress(),
            container.getLatitude(),
            container.getLongitude(),
            container.getContainerType(),
            container.getCapacity(),
            container.getStatus(),
            container.getSector().getId(),
            container.getSector().getName(),
            container.getSector().getMunicipality().getId(),
            container.getSector().getMunicipality().getName(),
            container.getCreatedAt(),
            container.getUpdatedAt()
        );
    }
}
