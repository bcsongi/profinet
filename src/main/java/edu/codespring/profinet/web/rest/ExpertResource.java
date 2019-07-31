package edu.codespring.profinet.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import edu.codespring.profinet.domain.Expert;
import edu.codespring.profinet.service.ExpertService;
import edu.codespring.profinet.service.ServiceException;
import edu.codespring.profinet.web.rest.dto.ExpertDTO;
import edu.codespring.profinet.web.rest.mapper.ExpertMapper;
import edu.codespring.profinet.web.rest.util.HeaderUtil;
import edu.codespring.profinet.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Expert.
 */
@RestController
@RequestMapping("/api")
public class ExpertResource{

    private final Logger log = LoggerFactory.getLogger(ExpertResource.class);
    
    @Inject
    private ExpertService expertService;

    @Inject
    private ExpertMapper expertMapper;

    /**
     * POST  /experts -> Create a new expert.
     */
    @RequestMapping(value = "/experts",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExpertDTO> create(@Valid @RequestBody ExpertDTO expertDTO) throws URISyntaxException {
        log.debug("REST request to save Expert : {}", expertDTO);
        Expert expert = expertMapper.expertDTOToExpert(expertDTO);
        Expert result;
        try {
        	result = expertService.create(expert);
        } catch (ServiceException ex){
        	return ResponseEntity.badRequest().header("Failure", "A new expert cannot already have an ID").body(null);
        }
        return ResponseEntity.created(new URI("/api/experts/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("expert", result.getId().toString()))
                .body(expertMapper.expertToExpertDTO(result));
    }

    /**
     * PUT  /experts -> Updates an existing expert.
     */
    @RequestMapping(value = "/experts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExpertDTO> update(@Valid @RequestBody ExpertDTO expertDTO) throws URISyntaxException {
    	log.debug("REST request to update Expert : {}", expertDTO);
        Expert expert = expertMapper.expertDTOToExpert(expertDTO);
        Expert result = expertService.update(expert);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("expert", expertDTO.getId().toString()))
                .body(expertMapper.expertToExpertDTO(result));
    }

    /**
     * GET  /experts -> get all the experts.
     */
    @RequestMapping(value = "/experts",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ExpertDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
    		@RequestParam(value = "per_page", required = false) Integer limit) throws URISyntaxException {
        Page<Expert> page = expertService.getAll(offset, limit);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/experts", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(expertMapper::expertToExpertDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }		
    		
    /**
     * GET  /experts -> get all the experts.
     */
    @RequestMapping(value = "/expertlist",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<ExpertDTO>> getAllFromIndex(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Expert> page = expertService.getAllFromIndex(offset, limit);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/experts", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(expertMapper::expertToExpertDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }
    
    /**
     * GET  /experts/:id -> get the "id" expert.
     */
    @RequestMapping(value = "/expertByLogin/{login}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExpertDTO> get(@PathVariable String login) {
        log.debug("REST request to get Expert : {} ", login);
        return Optional.ofNullable(expertService.get(login))
            .map(expertMapper::expertToExpertDTO)
            .map(expertDTO -> new ResponseEntity<>(expertDTO, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.ACCEPTED));
    }
    
    /**
     * GET  /getExpertInstance -> get the "id" expert.
     */
    @RequestMapping(value = "/getExpertInstance",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Expert getExpertInstance() {
    	Expert newExpert = new Expert();    	
    	return newExpert;
    }
    
    /**
     * PUT  /expertUpdateLastDate -> get the "id" expert.
     */
    @RequestMapping(value = "/expertUpdateLastDate",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExpertDTO> expertUpdateLastDate(@Valid @RequestBody Long id) {
    	log.debug("REST request to get expertUpdateLastDate id : {} ", id);
        
    	Expert expert = expertService.expertUpdateLastDate(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("expert", expert.getId().toString()))
            .body(expertMapper.expertToExpertDTO(expert));
    }

    /**
     * DELETE  /experts/:id -> delete the "id" expert.
     */
    @RequestMapping(value = "/experts/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Expert : {}", id);
        expertService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("expert", id.toString())).build();
    }

    /**
     * SEARCH  /_search/experts/:query -> search for the expert corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/experts/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Expert> search(@PathVariable String query) {
        return expertService.search(query);
    }
    
    /**
     * GET  /experts/:id -> get the "id" expert.
     */
    @RequestMapping(value = "/experts/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExpertDTO> getById(@PathVariable Long id) {
        log.debug("REST request to get Expert : {} ", id);
        return Optional.ofNullable(expertService.getById(id))
            .map(expertMapper::expertToExpertDTO)
            .map(expertDTO -> new ResponseEntity<>(expertDTO, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.ACCEPTED));
    }
    
}
