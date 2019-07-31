package edu.codespring.profinet.web.rest;

import com.codahale.metrics.annotation.Timed;

import edu.codespring.profinet.domain.Keyword;
import edu.codespring.profinet.service.KeywordService;
import edu.codespring.profinet.service.ServiceException;
import edu.codespring.profinet.web.rest.util.HeaderUtil;
import edu.codespring.profinet.web.rest.util.PaginationUtil;
import edu.codespring.profinet.web.rest.dto.KeywordDTO;
import edu.codespring.profinet.web.rest.mapper.KeywordMapper;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Keyword.
 */
@RestController
@RequestMapping("/api")
public class KeywordResource {

    private final Logger log = LoggerFactory.getLogger(KeywordResource.class);

    @Inject
    private KeywordService keywordService;

    @Inject
    private KeywordMapper keywordMapper;
    
    /**
     * POST  /keywords -> Create a new keyword.
     */
    @RequestMapping(value = "/keywords",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<KeywordDTO> create(@RequestBody KeywordDTO keywordDTO) throws URISyntaxException {
        log.debug("REST request to save Keyword : {}", keywordDTO);

        Keyword keyword = keywordMapper.keywordDTOToKeyword(keywordDTO);
        Keyword result;
        try {
        	result = keywordService.create(keyword);
        } catch (ServiceException ex){
        	return ResponseEntity.badRequest().header("Failure", "A new keyword cannot already have an ID").body(null);
        }
        return ResponseEntity.created(new URI("/api/keywords/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("keyword", result.getId().toString()))
                .body(keywordMapper.keywordToKeywordDTO(result));
    }

    /**
     * PUT  /keywords -> Updates an existing keyword.
     */
    @RequestMapping(value = "/keywords",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<KeywordDTO> update(@RequestBody KeywordDTO keywordDTO) throws URISyntaxException {
        log.debug("REST request to update Keyword : {}", keywordDTO);
        Keyword keyword = keywordMapper.keywordDTOToKeyword(keywordDTO);
        Keyword result = keywordService.update(keyword);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("keyword", keywordDTO.getId().toString()))
                .body(keywordMapper.keywordToKeywordDTO(result));
    }

    /**
     * GET  /keywords -> get all the keywords.
     */
    @RequestMapping(value = "/keywords",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<KeywordDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Keyword> page = keywordService.getAll(offset, limit);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/keywords", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(keywordMapper::keywordToKeywordDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /keywords/:id -> get the "id" keyword.
     */
    @RequestMapping(value = "/keywords/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<KeywordDTO> get(@PathVariable Long id) {
        log.debug("REST request to get Keyword : {}", id);
        return Optional.ofNullable(keywordService.get(id))
            .map(keywordMapper::keywordToKeywordDTO)
            .map(keywordDTO -> new ResponseEntity<>(
                keywordDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /keywords/:id -> delete the "id" keyword.
     */
    @RequestMapping(value = "/keywords/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Keyword : {}", id);
        keywordService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("keyword", id.toString())).build();
    }

    /**
     * SEARCH  /_search/keywords/:query -> search for the keyword corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/keywords/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Keyword> search(@PathVariable String query) {
        return keywordService.search(query);
    }
    
    
    /**
     * GET  /keywords/:id -> get the "id" keyword.
     */
    @RequestMapping(value = "/keywordsByName/{name}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Keyword> getByName(@PathVariable String name) {
        List<Keyword> result = null;
        boolean first = true;
        
    	String[] querys = name.split(" ");
        for (String query : querys) {
        	if (first) {
        		result = keywordService.getByName(query);
        		first = false;
        	}
        	List<Keyword> list = keywordService.getByName(query);
        	for (Keyword l : list) {
        		if (!result.contains(l)) {
        			result.add(l);
        		}
        	}
        }
        
    	return result;
    }
}
