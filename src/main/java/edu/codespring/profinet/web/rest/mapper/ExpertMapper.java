package edu.codespring.profinet.web.rest.mapper;

import edu.codespring.profinet.domain.*;
import edu.codespring.profinet.web.rest.dto.ExpertDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Expert and its DTO ExpertDTO.
 */
@Mapper(componentModel = "spring", uses = {KeywordMapper.class, LanguageMapper.class, UserMapper.class })
public interface ExpertMapper {

    ExpertDTO expertToExpertDTO(Expert expert);

    Expert expertDTOToExpert(ExpertDTO expertDTO);

    default Keyword keywordFromId(Long id) {
        if (id == null) {
            return null;
        }
        Keyword keyword = new Keyword();
        keyword.setId(id);
        return keyword;
    }

    default Language languageFromId(Long id) {
        if (id == null) {
            return null;
        }
        Language language = new Language();
        language.setId(id);
        return language;
    }
}
