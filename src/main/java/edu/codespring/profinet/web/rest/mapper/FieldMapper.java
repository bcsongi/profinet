package edu.codespring.profinet.web.rest.mapper;

import edu.codespring.profinet.domain.*;
import edu.codespring.profinet.web.rest.dto.FieldDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Field and its DTO FieldDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FieldMapper {

    FieldDTO fieldToFieldDTO(Field field);

    Field fieldDTOToField(FieldDTO fieldDTO);
}
