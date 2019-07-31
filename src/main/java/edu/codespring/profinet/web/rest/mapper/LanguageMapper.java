package edu.codespring.profinet.web.rest.mapper;

import edu.codespring.profinet.domain.*;
import edu.codespring.profinet.web.rest.dto.LanguageDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Language and its DTO LanguageDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LanguageMapper {

    LanguageDTO languageToLanguageDTO(Language language);

    Language languageDTOToLanguage(LanguageDTO languageDTO);
}
