package edu.codespring.profinet.web.rest;

import com.codahale.metrics.annotation.Timed;
import edu.codespring.profinet.domain.Language;
import edu.codespring.profinet.service.LanguageService;
import edu.codespring.profinet.web.rest.util.HeaderUtil;
import edu.codespring.profinet.web.rest.util.PaginationUtil;
import edu.codespring.profinet.web.rest.dto.LanguageDTO;
import edu.codespring.profinet.web.rest.mapper.LanguageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Language.
 */
@RestController
@RequestMapping("/api")
public class LanguageResource {

    private final Logger log = LoggerFactory.getLogger(LanguageResource.class);

    @Inject
    private LanguageService languageService;
    
    @Inject
    private LanguageMapper languageMapper;

    /**
     * POST  /languages -> Create a new language.
     */
    @RequestMapping(value = "/languages",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LanguageDTO> create(@Valid @RequestBody LanguageDTO languageDTO) throws URISyntaxException {
        log.debug("REST request to save Language : {}", languageDTO);
        if (languageDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new language cannot already have an ID").body(null);
        }
        Language language = languageMapper.languageDTOToLanguage(languageDTO);
        Language result = languageService.create(language);
        return ResponseEntity.created(new URI("/api/languages/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("language", result.getId().toString()))
                .body(languageMapper.languageToLanguageDTO(result));
    }

    /**
     * PUT  /languages -> Updates an existing language.
     */
    @RequestMapping(value = "/languages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LanguageDTO> update(@Valid @RequestBody LanguageDTO languageDTO) throws URISyntaxException {
        log.debug("REST request to update Language : {}", languageDTO);
  
        Language language = languageMapper.languageDTOToLanguage(languageDTO);
        Language result = languageService.update(language);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("language", languageDTO.getId().toString()))
                .body(languageMapper.languageToLanguageDTO(result));
    }

    /**
     * GET  /languages -> get all the languages.
     */
    @RequestMapping(value = "/languages",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<LanguageDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Language> page = languageService.getAll(offset, limit);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/languages", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(languageMapper::languageToLanguageDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /languages/:id -> get the "id" language.
     */
    @RequestMapping(value = "/languages/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LanguageDTO> get(@PathVariable Long id) {
        log.debug("REST request to get Language : {}", id);
        return Optional.ofNullable(languageService.get(id))
            .map(languageMapper::languageToLanguageDTO)
            .map(languageDTO -> new ResponseEntity<>(
                languageDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /languages/:id -> delete the "id" language.
     */
    @RequestMapping(value = "/languages/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Language : {}", id);
        languageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("language", id.toString())).build();
    }

    /**
     * SEARCH  /_search/languages/:query -> search for the language corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/languages/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Language> search(@PathVariable String query) {
        return languageService.search(query);
    }
}
