package com.example.demodaraasapuwaservice.mapper;

import com.example.demodaraasapuwaservice.dao.PaymentReasonEntity;
import com.example.demodaraasapuwaservice.dto.PaymentReasonDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class PaymentReasonMapper {
    public abstract List<PaymentReasonDto> mapLDaoToLDto(List<PaymentReasonEntity> PaymentReasonEntityList);

    public abstract PaymentReasonDto mapDaoToDto(PaymentReasonEntity PaymentReasonEntity);

    @Mapping(target = "lastModifyDate", ignore = true)
    @Mapping(target = "addedDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract PaymentReasonEntity mapDtoToDao(PaymentReasonDto PaymentReasonDto);

    @Mapping(target = "addedDate", ignore = true)
    @Mapping(target = "lastModifyDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract PaymentReasonEntity modDtoToDao(PaymentReasonDto PaymentReasonDto, @MappingTarget PaymentReasonEntity PaymentReasonEntity);

}
