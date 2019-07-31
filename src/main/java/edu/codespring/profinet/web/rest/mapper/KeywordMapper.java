package edu.codespring.profinet.web.rest.mapper;

import edu.codespring.profinet.domain.*;
import edu.codespring.profinet.web.rest.dto.KeywordDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Keyword and its DTO KeywordDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface KeywordMapper {

    KeywordDTO keywordToKeywordDTO(Keyword keyword);

    Keyword keywordDTOToKeyword(KeywordDTO keywordDTO);
}
