package edu.codespring.profinet.web.rest;

import com.codahale.metrics.annotation.Timed;

import edu.codespring.profinet.domain.Field;
import edu.codespring.profinet.service.FieldService;
import edu.codespring.profinet.service.ServiceException;
import edu.codespring.profinet.web.rest.util.HeaderUtil;
import edu.codespring.profinet.web.rest.util.PaginationUtil;
import edu.codespring.profinet.web.rest.dto.FieldDTO;
import edu.codespring.profinet.web.rest.mapper.FieldMapper;

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
 * REST controller for managing Field.
 */
@RestController
@RequestMapping("/api")
public class FieldResource {

    private final Logger log = LoggerFactory.getLogger(FieldResource.class);

    @Inject
    private FieldService fieldService;

    @Inject
    private FieldMapper fieldMapper;

    /**
     * POST  /fields -> Create a new field.
     */
    @RequestMapping(value = "/fields",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FieldDTO> create(@Valid @RequestBody FieldDTO fieldDTO) throws URISyntaxException {
        log.debug("REST request to save Field : {}", fieldDTO);
        Field field = fieldMapper.fieldDTOToField(fieldDTO);
        Field result;
        try {
        	result = fieldService.create(field);
        } catch (ServiceException ex){
        	return ResponseEntity.badRequest().header("Failure", "A new field cannot already have an ID").body(null);
        }
        return ResponseEntity.created(new URI("/api/fields/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("field", result.getId().toString()))
                .body(fieldMapper.fieldToFieldDTO(result));
    }

    /**
     * PUT  /fields -> Updates an existing field.
     */
    @RequestMapping(value = "/fields",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FieldDTO> update(@Valid @RequestBody FieldDTO fieldDTO) throws URISyntaxException {
        log.debug("REST request to update Field : {}", fieldDTO);
        Field field = fieldMapper.fieldDTOToField(fieldDTO);
        Field result = fieldService.update(field);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("field", fieldDTO.getId().toString()))
                .body(fieldMapper.fieldToFieldDTO(result));
    }

    /**
     * GET  /fields -> get all the fields.
     */
    @RequestMapping(value = "/fields",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<FieldDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Field> page = fieldService.getAll(offset, limit);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fields", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(fieldMapper::fieldToFieldDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /fields/:id -> get the "id" field.
     */
    @RequestMapping(value = "/fields/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FieldDTO> get(@PathVariable Long id) {
        log.debug("REST request to get Field : {}", id);
        return Optional.ofNullable(fieldService.get(id))
            .map(fieldMapper::fieldToFieldDTO)
            .map(fieldDTO -> new ResponseEntity<>(
                fieldDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /fieldsByExpertId/:id -> get the "id" field.
     */
    @RequestMapping(value = "/fieldsByExpertId/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Field> fieldsByExpertId(@PathVariable Long id) {
    	return fieldService.fieldsByExpertId(id);
    }    
    
    /**
     * DELETE  /fields/:id -> delete the "id" field.
     */
    @RequestMapping(value = "/fields/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Field : {}", id);
        fieldService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("field", id.toString())).build();
    }

    /**
     * SEARCH  /_search/fields/:query -> search for the field corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/fields/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Field> search(@PathVariable String query) {
        return fieldService.search(query);
    }
}
