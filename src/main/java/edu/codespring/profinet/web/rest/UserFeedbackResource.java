package edu.codespring.profinet.web.rest;

import com.codahale.metrics.annotation.Timed;

import edu.codespring.profinet.domain.UserFeedback;
import edu.codespring.profinet.service.ServiceException;
import edu.codespring.profinet.service.UserFeedbackService;
import edu.codespring.profinet.web.rest.dto.UserFeedbackDTO;
import edu.codespring.profinet.web.rest.mapper.UserFeedbackMapper;
import edu.codespring.profinet.web.rest.util.HeaderUtil;
import edu.codespring.profinet.web.rest.util.PaginationUtil;

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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing UserFeedback.
 */
@RestController
@RequestMapping("/api")
public class UserFeedbackResource {

    private final Logger log = LoggerFactory.getLogger(UserFeedbackResource.class);

    @Inject
    private UserFeedbackService userFeedbackService;
    
    @Inject
    private UserFeedbackMapper userFeedbackMapper;
    
    /**
     * POST  /userFeedbacks -> Create a new userFeedback.
     */
    @RequestMapping(value = "/userFeedbacks",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserFeedbackDTO> create(@RequestBody UserFeedbackDTO userFeedbackDTO) throws URISyntaxException {
        log.debug("REST request to save UserFeedback : {}", userFeedbackDTO);
        
        UserFeedback userFeedback = userFeedbackMapper.userFeedbackDTOTOUserFeedback(userFeedbackDTO);
        UserFeedbackDTO result;
        try{
        	result = userFeedbackMapper.userFeedbackToUserFeedbackDTO(userFeedbackService.create(userFeedback));
        } catch (ServiceException ex){
        	return ResponseEntity.badRequest().header("Failure", "A new userFeedback cannot already have an ID").body(null);
        }
        return ResponseEntity.created(new URI("/api/userFeedbacks/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("userFeedback", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /userFeedbacks -> Updates an existing userFeedback.
     */
    @RequestMapping(value = "/userFeedbacks",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserFeedbackDTO> update(@RequestBody UserFeedbackDTO userFeedbackDTO) throws URISyntaxException {
        log.debug("REST request to update UserFeedback : {}", userFeedbackDTO);
        UserFeedback userFeedback = userFeedbackMapper.userFeedbackDTOTOUserFeedback(userFeedbackDTO);
        UserFeedbackDTO result = userFeedbackMapper.userFeedbackToUserFeedbackDTO(userFeedbackService.update(userFeedback));
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("userFeedback", userFeedback.getId().toString()))
                .body(result);
    }

    /**
     * GET  /userFeedbacks -> get all the userFeedbacks.
     */
    @RequestMapping(value = "/userFeedbacks",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<UserFeedbackDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<UserFeedback> page = userFeedbackService.getAll(offset, limit);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/userFeedbacks", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
        		.map(userFeedbackMapper::userFeedbackToUserFeedbackDTO)
        		.collect(Collectors.toList()), headers, HttpStatus.OK);
    }

    /**
     * GET  /userFeedbacks/:id -> get the "id" userFeedback.
     */
    @RequestMapping(value = "/userFeedbacks/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserFeedbackDTO> get(@PathVariable Long id) {
        log.debug("REST request to get UserFeedback : {}", id);
        return Optional.ofNullable(userFeedbackService.get(id))
        		.map(userFeedbackMapper::userFeedbackToUserFeedbackDTO)
            .map(userFeedbackDTO -> new ResponseEntity<>(
                userFeedbackDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    /**
     * GET  /userFeedbackByAppointment/:id -> get the "id" userFeedback.
     */
    @RequestMapping(value = "/userFeedbackByAppointment/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserFeedbackDTO> getUserFeedbackByAppointment(@PathVariable Long id) {
    	return Optional.ofNullable(userFeedbackService.getUserFeedbackByAppointment(id))
    			.map(userFeedbackMapper::userFeedbackToUserFeedbackDTO)
            .map(userFeedbackDTO -> new ResponseEntity<>(
                userFeedbackDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.OK));
    }

    /**
     * DELETE  /userFeedbacks/:id -> delete the "id" userFeedback.
     */
    @RequestMapping(value = "/userFeedbacks/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete UserFeedback : {}", id);
        userFeedbackService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userFeedback", id.toString())).build();
    }

    /**
     * SEARCH  /_search/userFeedbacks/:query -> search for the userFeedback corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/userFeedbacks/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<UserFeedbackDTO> search(@PathVariable String query) {
        return StreamSupport
        		.stream(userFeedbackService.search(query).spliterator(), false)
        		.map(userFeedbackMapper::userFeedbackToUserFeedbackDTO)
        		.collect(Collectors.toList());
    }
    
    /**
     * SEARCH  /_search/userFeedbacksByExpertField/expertFieldId -> search for the userFeedback corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/userFeedbacksByExpertField/{expertFieldId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        public List<UserFeedbackDTO> searchByExpertField(@PathVariable Long expertFieldId) {
            return StreamSupport
            		.stream(userFeedbackService.searchByExpertField(expertFieldId).spliterator(), false)
            		.map(userFeedbackMapper::userFeedbackToUserFeedbackDTO)
            		.collect(Collectors.toList());
        }

    /**
     * GET  /userFeedbacks/:id -> get the "id" userFeedback.
     * @return 
     * @throws URISyntaxException 
     */
    @RequestMapping(value = "/userFeedbacksRating/{rating}&{comment}&{appointmentid}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    public List<UserFeedbackDTO> setExpertFieldByUserFeedback(@PathVariable Integer rating, @PathVariable String comment, @PathVariable Long appointmentid) throws URISyntaxException {
    	return StreamSupport
    			.stream(userFeedbackService.setExpertFieldByUserFeedback(rating, comment, appointmentid).spliterator(), false)
    			.map(userFeedbackMapper::userFeedbackToUserFeedbackDTO)
    			.collect(Collectors.toList());
    }
}
