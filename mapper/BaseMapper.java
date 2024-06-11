package com.example.iotDataGenerator.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface BaseMapper<E,D> {

    public D convertEntityToDto(E entity);
    public E convertDtoToEntity(D dto);

    List<D> convertEntitiesToDtos(List<E> entities);

    Set<D> convertEntitiesToDtos(Set<E> entities);

    default List<D> convertEntitiesToDtosDefault(List<E> entities) {
        return entities.stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    default List<E> convertDtosToEntitiesDefault(List<D> dtos) {
        return dtos.stream().map(this:: convertDtoToEntity).collect(Collectors.toList());
    }

    default Set<D> convertEntitiesToDtosSet(Set<E> entities) {
        return entities.stream().map(this::convertEntityToDto).collect(Collectors.toSet());
    }

    default Set<E> convertDtosToEntitiesSet(Set<D> dtos) {
        return dtos.stream().map(this::convertDtoToEntity).collect(Collectors.toSet());
    }


    default Page<D> convertPageToDto(Page<E> page) {
        List<D> content = convertEntitiesToDtosDefault(page.getContent());
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }
    /**
     * Partially updates an entity based on the provided data transfer object (DTO).
     *
     * @param entity The entity to be partially updated.
     * @param dto    The data transfer object containing the partial updates.
     *               It should only include the fields that need to be updated in the entity.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void updateEntityFromDTO(D dto, @MappingTarget E entity);}
