package com.example.demo_basic.mapper;

import com.example.demo_basic.dto.SolicitudDTO;
import com.example.demo_basic.model.entity.Solicitud;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SolicitudMapper {
    SolicitudDTO toDTO(Solicitud solicitud);
}
