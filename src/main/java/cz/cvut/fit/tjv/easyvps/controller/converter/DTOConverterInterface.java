package cz.cvut.fit.tjv.easyvps.controller.converter;

public interface DTOConverterInterface<DTO, Entity> {

    DTO toDTO(Entity entity);

    Entity toEntity(DTO dto);
}
