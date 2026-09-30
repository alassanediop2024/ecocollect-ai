package com.ecocollect.mapper;

import com.ecocollect.dto.CollectionResponse;
import com.ecocollect.model.Collection;
import org.springframework.stereotype.Component;

@Component
public class CollectionMapper {

    public CollectionResponse toResponse(Collection collection) {
        return new CollectionResponse(
            collection.getId(),
            collection.getContainer().getId(),
            collection.getContainer().getCode(),
            collection.getContainer().getSector().getId(),
            collection.getContainer().getSector().getName(),
            collection.getContainer().getSector().getMunicipality().getId(),
            collection.getContainer().getSector().getMunicipality().getName(),
            collection.getCollectionDate(),
            collection.getWeightKg(),
            collection.getStatus(),
            collection.getOperator(),
            collection.getNotes(),
            collection.getCreatedAt(),
            collection.getUpdatedAt()
        );
    }
}
