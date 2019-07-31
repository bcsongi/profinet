package edu.codespring.profinet.web.rest;

import com.codahale.metrics.annotation.Timed;
import edu.codespring.profinet.domain.ExpertContactRequest;
import edu.codespring.profinet.service.ExpertContactRequestService;
import edu.codespring.profinet.service.ServiceException;
import edu.codespring.profinet.web.rest.dto.ExpertContactRequestDTO;
import edu.codespring.profinet.web.rest.mapper.ExpertContactRequestMapper;
import edu.codespring.profinet.web.rest.util.HeaderUtil;
import edu.codespring.profinet.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing ExpertContactRequest.
 */
@RestController
@RequestMapping("/api")
public class ExpertContactRequestResource {

    private final Logger log = LoggerFactory.getLogger(ExpertContactRequestResource.class);

    @Inject
    private ExpertContactRequestService expertContactRequestService;
    
    @Inject
    private ExpertContactRequestMapper expertContactRequestMapper;
    
    /**
     * POST  /expertContactRequests -> Create a new expertContactRequest.
     */
    @RequestMapping(value = "/expertContactRequests",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExpertContactRequestDTO> create(@RequestBody ExpertContactRequestDTO expertContactRequestDTO) throws URISyntaxException {
        log.debug("REST request to save ExpertContactRequest : {}", expertContactRequestDTO);
        if (expertContactRequestDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new expertContactRequest cannot already have an ID").body(null);
        }
        ExpertContactRequest expertContactRequest = expertContactRequestMapper.expertContactRequestDTOToExpertContactRequest(expertContactRequestDTO);
        try{
        	expertContactRequest = expertContactRequestService.create(expertContactRequest);
        } catch (ServiceException ex){
        	return ResponseEntity.badRequest().header("Failure", "A new expertContactRequest cannot already have an ID").body(null);
        }
        ExpertContactRequestDTO result = expertContactRequestMapper.expertContactRequestToExpertContactRequestDTO(expertContactRequest);
        return ResponseEntity.created(new URI("/api/expertContactRequests/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("expertContactRequest", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /expertContactRequests -> Updates an existing expertContactRequest.
     */
    @RequestMapping(value = "/expertContactRequests",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExpertContactRequestDTO> update(@RequestBody ExpertContactRequestDTO expertContactRequestDTO) throws URISyntaxException {
        log.debug("REST request to update ExpertContactRequestDTO : {}", expertContactRequestDTO);
        ExpertContactRequest expertContactRequest = expertContactRequestMapper.expertContactRequestDTOToExpertContactRequest(expertContactRequestDTO);
        ExpertContactRequestDTO result;
        try{
        	result = expertContactRequestMapper.expertContactRequestToExpertContactRequestDTO(expertContactRequestService.update(expertContactRequest));
        } catch (ServiceException ex){
        	return create(expertContactRequestDTO);
        }
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("expertContactRequestDTO", result.getId().toString()))
                .body(result);
    }

    /**
     * GET  /expertContactRequests -> get all the expertContactRequests.
     */
    @RequestMapping(value = "/expertContactRequests",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ExpertContactRequestDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<ExpertContactRequest> page = expertContactRequestService.getAll(offset, limit);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/expertContactRequests", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
                .map(expertContactRequestMapper::expertContactRequestToExpertContactRequestDTO)
                .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK); 
    }

    /**
     * GET  /expertContactRequests/:id -> get the "id" expertContactRequest.
     */
    @RequestMapping(value = "/expertContactRequests/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExpertContactRequestDTO> get(@PathVariable Long id) {
        log.debug("REST request to get ExpertContactRequest : {}", id);
        return Optional.ofNullable(expertContactRequestService.get(id))
        		.map(expertContactRequestMapper::expertContactRequestToExpertContactRequestDTO)
        		.map(expertContactRequestDTO -> new ResponseEntity<>(
                expertContactRequestDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /expertContactRequests/:id -> delete the "id" expertContactRequest.
     */
    @RequestMapping(value = "/expertContactRequests/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete ExpertContactRequest : {}", id);
        expertContactRequestService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("expertContactRequest", id.toString())).build();
    }

    /**
     * SEARCH  /_search/expertContactRequests/:query -> search for the expertContactRequest corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/expertContactRequests/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ExpertContactRequestDTO> search(@PathVariable String query) {
        return StreamSupport
        		.stream(expertContactRequestService.search(query).spliterator(), false)
        		.map(expertContactRequestMapper::expertContactRequestToExpertContactRequestDTO)
        		.collect(Collectors.toList());
    }
    
    /**
     * GET  /_search/expertContactRequests/:expert&:user -> search for the expertContactRequest corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/expertContactRequestsStatusBetween/{expert}&{user}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ExpertContactRequestDTO> getStatusBetween(@PathVariable Long expert, @PathVariable String user) {
        log.debug("REST request to get ExpertContactRequest : expert: {} user: {}", expert, user);
        return StreamSupport
        		.stream(expertContactRequestService.getStatusBetween(expert, user).spliterator(), false)
        		.map(expertContactRequestMapper::expertContactRequestToExpertContactRequestDTO)
        		.collect(Collectors.toList());
    }
    
    /**
     * GET  /expertContactRequests/:id -> get the "id" expertContactRequest.
     */
    @RequestMapping(value = "/_search/expertContactRequestsTo/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ExpertContactRequestDTO>> getExpertContactRequestsTo(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit, @PathVariable Long id)
        throws URISyntaxException {
        Page<ExpertContactRequest> page = expertContactRequestService.getExpertContactRequestsTo(offset, limit, id);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/expertFieldsList", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
        		.map(expertContactRequestMapper::expertContactRequestToExpertContactRequestDTO)
        		.collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }
}
