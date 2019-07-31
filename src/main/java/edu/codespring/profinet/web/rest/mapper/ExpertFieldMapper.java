package edu.codespring.profinet.web.rest.mapper;

import org.mapstruct.Mapper;

import edu.codespring.profinet.domain.Expert;
import edu.codespring.profinet.domain.ExpertField;
import edu.codespring.profinet.domain.Field;
import edu.codespring.profinet.web.rest.dto.ExpertFieldDTO;

@Mapper(componentModel = "spring", uses = {ExpertMapper.class, FieldMapper.class })
public interface ExpertFieldMapper {

	ExpertFieldDTO expertFieldToExpertFieldDTO(ExpertField expertField);

    ExpertField expertFieldDTOToExpertField(ExpertFieldDTO expertFieldDTO);

    default Expert expertFromId(Long id) {
        if (id == null) {
            return null;
        }
        Expert expert = new Expert();
        expert.setId(id);
        return expert;
    }
    
    default Field fieldFromId(Long id) {
        if (id == null) {
            return null;
        }
        Field field = new Field();
        field.setId(id);
        return field;
    }


}
