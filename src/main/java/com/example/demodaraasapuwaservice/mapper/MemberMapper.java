package com.example.demodaraasapuwaservice.mapper;

import com.example.demodaraasapuwaservice.dao.MemberEntity;
import com.example.demodaraasapuwaservice.dto.MemberDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class MemberMapper {
    public abstract List<MemberDto> mapLDaoToLDto(List<MemberEntity> MemberEntityList);

    public abstract MemberDto mapDaoToDto(MemberEntity MemberEntity);

    @Mapping(target = "lastModifyDate", ignore = true)
    @Mapping(target = "addedDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract MemberEntity mapDtoToDao(MemberDto MemberDto);

    @Mapping(target = "addedDate", ignore = true)
    @Mapping(target = "lastModifyDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract MemberEntity modDtoToDao(MemberDto MemberDto, @MappingTarget MemberEntity MemberEntity);
}
