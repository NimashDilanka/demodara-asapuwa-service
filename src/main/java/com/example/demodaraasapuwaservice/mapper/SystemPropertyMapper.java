package com.example.demodaraasapuwaservice.mapper;

import com.example.demodaraasapuwaservice.dao.SystemPropertyEntity;
import com.example.demodaraasapuwaservice.dto.SystemPropertyDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class SystemPropertyMapper {
    public abstract List<SystemPropertyDto> mapLDaoToLDto(List<SystemPropertyEntity> systemPropertyEntityList);

    public abstract SystemPropertyDto mapDaoToDto(SystemPropertyEntity systemPropertyEntity);

    @Mapping(target = "lastModifyDate", ignore = true)
    @Mapping(target = "addedDate", ignore = true)
    public abstract SystemPropertyEntity mapDtoToDao(SystemPropertyDto systemPropertyDto);

    @Mapping(target = "addedDate", ignore = true)
    @Mapping(target = "lastModifyDate", ignore = true)
    public abstract SystemPropertyEntity modDtoToDao(SystemPropertyDto systemPropertyDto, @MappingTarget SystemPropertyEntity systemPropertyEntity);
}
