package com.pm.alexki.patientservice.mapper;

import com.pm.alexki.patientservice.dto.PatientRequestDto;
import com.pm.alexki.patientservice.dto.PatientResponseDto;
import com.pm.alexki.patientservice.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PatientDtoMapper {

    PatientResponseDto toDto(Patient patient);

    @Mapping(target = "id", ignore = true)
    Patient toEntity(PatientRequestDto dto);
}
