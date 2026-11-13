package com.example.lmslite.exception.notFound;

import lombok.Getter;

import java.util.UUID;

@Getter
public class EntityNotFoundException extends RuntimeException
{
    private final String entityName;
    private Long entityId;
    private UUID entityUUID;
    public EntityNotFoundException(String entityName, Long entityId)
    {
        super("%s with id %s not found".formatted(entityName, entityId.toString()));
        this.entityName = entityName;
        this.entityId = entityId;
    }

    public EntityNotFoundException(String entityName, UUID entityUUID)
    {
        super("%s with id %s not found".formatted(entityName, entityUUID.toString()));
        this.entityName = entityName;
        this.entityUUID = entityUUID;
    }

}
