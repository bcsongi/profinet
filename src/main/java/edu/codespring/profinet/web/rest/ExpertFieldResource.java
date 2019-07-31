package edu.codespring.profinet.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import edu.codespring.profinet.domain.ExpertField;
import edu.codespring.profinet.service.ExpertFieldService;
import edu.codespring.profinet.service.ServiceException;
import edu.codespring.profinet.web.rest.dto.ExpertFieldDTO;
import edu.codespring.profinet.web.rest.mapper.ExpertFieldMapper;
import edu.codespring.profinet.web.rest.util.HeaderUtil;
import edu.codespring.profinet.web.rest.util.PaginationUtil;

/**
 * REST controller for managing ExpertField.
 */
@RestController
@RequestMapping("/api")
public class ExpertFieldResource {
	   private final Logger log = LoggerFactory.getLogger(ExpertFieldResource.class);

	   @Inject
	   private ExpertFieldService expertFieldService;
	   
	   @Inject
	   private ExpertFieldMapper expertFieldMapper;
	   
	    /**
	     * POST  /expertField -> Create a new expertField.
	     */
	    @RequestMapping(value = "/expertFields",
	            method = RequestMethod.POST,
	            produces = MediaType.APPLICATION_JSON_VALUE)
	    @Timed
	    public ResponseEntity<ExpertFieldDTO> create(@RequestBody ExpertFieldDTO expertFieldDTO) throws URISyntaxException {
	        log.debug("REST request to save ExpertField : {}", expertFieldDTO);
	        ExpertField expertField = expertFieldMapper.expertFieldDTOToExpertField(expertFieldDTO);
	        ExpertFieldDTO result;
	        try{
	        	result = expertFieldMapper.expertFieldToExpertFieldDTO(expertFieldService.create(expertField));
	        } catch (ServiceException ex){
	        	return ResponseEntity.badRequest().header("Failure", "A new expertField cannot already have an ID").body(null);
	        }
	        return ResponseEntity.created(new URI("/api/expertFields/" + result.getId()))
	                .headers(HeaderUtil.createEntityCreationAlert("expertField", result.getId().toString()))
	                .body(result);
	    }

	    /**
	     * PUT  /expertFields -> Updates an existing expertField.
	     */
	    @RequestMapping(value = "/expertFields",
	        method = RequestMethod.PUT,
	        produces = MediaType.APPLICATION_JSON_VALUE)
	    @Timed
	    public ResponseEntity<ExpertFieldDTO> update(@RequestBody ExpertFieldDTO expertFieldDTO) throws URISyntaxException {
	        log.debug("REST request to update ExpertField : {}", expertFieldDTO);
	        ExpertField expertField = expertFieldMapper.expertFieldDTOToExpertField(expertFieldDTO);
	        ExpertFieldDTO result = expertFieldMapper.expertFieldToExpertFieldDTO(expertFieldService.update(expertField));
	        return ResponseEntity.ok()
	                .headers(HeaderUtil.createEntityUpdateAlert("expertField", expertField.getId().toString()))
	                .body(result);
	    }

	    /**
	     * GET  /expertFields -> get all the expertFields.
	     */
	    @RequestMapping(value = "/expertFields",
	            method = RequestMethod.GET,
	            produces = MediaType.APPLICATION_JSON_VALUE)
	    @Timed
	    public ResponseEntity<List<ExpertFieldDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
	                                  @RequestParam(value = "per_page", required = false) Integer limit)
	        throws URISyntaxException {
	        Page<ExpertField> page = expertFieldService.getAll(offset, limit);
	        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/expertFields", offset, limit);
	        return new ResponseEntity<>(page.getContent().stream()
	        		.map(expertFieldMapper::expertFieldToExpertFieldDTO)
	        		.collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
	    }
	    
	    /**
	     * GET  /expertFields -> get all the expertFields from index.
	     */
	    @RequestMapping(value = "/expertFieldsList",
	            method = RequestMethod.GET,
	            produces = MediaType.APPLICATION_JSON_VALUE)
	    @Timed
	    public ResponseEntity<List<ExpertFieldDTO>> getAllFromIndex(@RequestParam(value = "page" , required = false) Integer offset,
	                                  @RequestParam(value = "per_page", required = false) Integer limit)
	        throws URISyntaxException {
	        Page<ExpertField> page = expertFieldService.getAllFromIndex(offset, limit);
	        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/expertFieldsList", offset, limit);
	        return new ResponseEntity<>(page.getContent().stream()
	        		.map(expertFieldMapper::expertFieldToExpertFieldDTO)
	        		.collect(Collectors.toList()), headers, HttpStatus.OK);
	    }

	    /**
	     * GET  /expertFields/:id -> get the "id" expertField.
	     */
	    @RequestMapping(value = "/expertFields/{id}",
	            method = RequestMethod.GET,
	            produces = MediaType.APPLICATION_JSON_VALUE)
	    @Timed
	    public ResponseEntity<ExpertFieldDTO> get(@PathVariable Long id) {
	        log.debug("REST request to get ExpertField : {}", id);
	        return Optional.ofNullable(expertFieldService.get(id))
	        		.map(expertFieldMapper::expertFieldToExpertFieldDTO)
	            .map(expertFieldDTO -> new ResponseEntity<>(
	                expertFieldDTO,
	                HttpStatus.OK))
	            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	    }

	    /**
	     * DELETE  /expertFields/:id -> delete the "id" expertField.
	     */
	    @RequestMapping(value = "/expertFields/{id}",
	            method = RequestMethod.DELETE,
	            produces = MediaType.APPLICATION_JSON_VALUE)
	    @Timed
	    public ResponseEntity<Void> delete(@PathVariable Long id) {
	        log.debug("REST request to delete ExpertField : {}", id);
	        expertFieldService.delete(id);
	        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("expertField", id.toString())).build();
	    }

	    /**
	     * SEARCH  /_search/expertFields/:query -> search for the expertField corresponding
	     * to the query.
	     */
	    @RequestMapping(value = "/_search/expertFields/{query}",
	        method = RequestMethod.GET,
	        produces = MediaType.APPLICATION_JSON_VALUE)
	    @Timed
	    public List<ExpertFieldDTO> search(@PathVariable String query) {
	        return StreamSupport
	        		.stream(expertFieldService.search(query).spliterator(), false)
	        		.map(expertFieldMapper::expertFieldToExpertFieldDTO)
	        		.collect(Collectors.toList());
	    }
	    
	    /**
	     * SEARCH  /_search/expertFields/:query -> search for the expertField corresponding
	     * to the query.
	     */
	    @RequestMapping(value = "/_search/expertFieldsActiveExperts/{query}",
	        method = RequestMethod.GET,
	        produces = MediaType.APPLICATION_JSON_VALUE)
	    @Timed
	    public List<ExpertFieldDTO> searchActives(@PathVariable String query) {
	    	////THIS HAS TO BE MODIFIED!!!!
	    	
	        return StreamSupport
	        		.stream(expertFieldService.searchActives(query).spliterator(), false)
	        		.map(expertFieldMapper::expertFieldToExpertFieldDTO)
	        		.collect(Collectors.toList());
	    }
	    
	    /**
	     * FILTER  /_search/expertFields/category?:query -> search for the expertField corresponding
	     * to the query.
	     */
	    @RequestMapping(value = "/_search/expertFieldsFilterByCategory/{category}",
	        method = RequestMethod.GET,
	        produces = MediaType.APPLICATION_JSON_VALUE)
	    @Timed
	    public List<ExpertFieldDTO> filterByCategory(@PathVariable String category) {
	        return StreamSupport
	        		.stream(expertFieldService.filterByCategory(category).spliterator(), false)
	        		.map(expertFieldMapper::expertFieldToExpertFieldDTO)
	        		.collect(Collectors.toList());
	    }
	    
	    /**
	     * FILTER  /_search/expertFields/category?:query -> search for the expertField corresponding
	     * to the query.
	     */
	    @RequestMapping(value = "/_search/expertFieldsFilterByLanguage/{language}",
	        method = RequestMethod.GET,
	        produces = MediaType.APPLICATION_JSON_VALUE)
	    @Timed
	    public List<ExpertFieldDTO> filterByLanguage(@PathVariable String language) {
	        return StreamSupport
	        		.stream(expertFieldService.filterByLanguage(language).spliterator(), false)
	        		.map(expertFieldMapper::expertFieldToExpertFieldDTO)
	        		.collect(Collectors.toList());
	    }
	    
	    /**
	     * FILTER  /_search/expertFieldsFilter/category?:query -> search for the expertField corresponding
	     * to the query.
	     */
	    @RequestMapping(value = "/_search/expertFieldsFilter/{category}&{language}",
	        method = RequestMethod.GET,
	        produces = MediaType.APPLICATION_JSON_VALUE)
	    @Timed
	    public List<ExpertFieldDTO> filterByCategoryAndLanguage(@PathVariable String category, @PathVariable String language) {
	        return StreamSupport
	        		.stream(expertFieldService.filterByCategoryAndLanguage(category, language).spliterator(), false)
	        		.map(expertFieldMapper::expertFieldToExpertFieldDTO)
	        		.collect(Collectors.toList());
	    }
	    
	    /**
	     * FILTER  /expertFieldsByRating -> search for the expertField corresponding
	     * to the query.
	     */
	    @RequestMapping(value = "/expertFieldsByRating",
	        method = RequestMethod.GET,
	        produces = MediaType.APPLICATION_JSON_VALUE)
	    @Timed
	    public List<ExpertFieldDTO> expertFieldsByRating() {
	        return StreamSupport
	        		.stream(expertFieldService.expertFieldsByRating().spliterator(), false)
	        		.map(expertFieldMapper::expertFieldToExpertFieldDTO)
	        		.collect(Collectors.toList());
	    }
	    
	    /**
	     * SEARCH  /_search/expertFieldsActive/:query -> search for the expertField corresponding
	     * to the query.
	     */
	    @RequestMapping(value="/_search/expertFieldsActive/{query}",
	    		method = RequestMethod.GET,
	    		produces = MediaType.APPLICATION_JSON_VALUE)
	    @Timed
	    public List<ExpertFieldDTO> searchInActiveExperts(@PathVariable String query){
	    	return StreamSupport
	    			.stream(expertFieldService.searchInActiveExperts(query).spliterator(), false)
	    			.map(expertFieldMapper::expertFieldToExpertFieldDTO)
	    			.collect(Collectors.toList());
	    }
	    
	    /**
	     * GET  /expertFieldsByUserLogin/:login -> get all the expertFields from index.
	     */
	    @RequestMapping(value = "/expertFieldsByUserLogin/{login}",
	            method = RequestMethod.GET,
	            produces = MediaType.APPLICATION_JSON_VALUE)
	    @Timed
	    public ResponseEntity<List<ExpertFieldDTO>> expertFieldsByUserLogin(@PathVariable String login) {	
    		return Optional.ofNullable(StreamSupport
    				.stream(expertFieldService.expertFieldsByUserLogin(login).spliterator(),false)
    				.map(expertFieldMapper::expertFieldToExpertFieldDTO)
    				.collect(Collectors.toList()))
	            .map(expertFieldDTO -> new ResponseEntity<>(
	                expertFieldDTO,
	                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	    }
	    
	}
