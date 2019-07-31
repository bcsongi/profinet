package edu.codespring.profinet.web.rest;

import com.codahale.metrics.annotation.Timed;
import edu.codespring.profinet.domain.Appointment;
import edu.codespring.profinet.service.AppointmentService;
import edu.codespring.profinet.web.rest.dto.AppointmentDTO;
import edu.codespring.profinet.web.rest.mapper.AppointmentMapper;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing Appointment.
 */
@RestController
@RequestMapping("/api")
public class AppointmentResource {

    private final Logger log = LoggerFactory.getLogger(AppointmentResource.class);
    
    @Inject
    private AppointmentService appointmentService;
    
    @Inject
    private AppointmentMapper appointmentMapper;
    
    /**
     * POST  /appointments -> Create a new appointment.
     */
    @RequestMapping(value = "/appointments",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    public ResponseEntity<AppointmentDTO> create(@Valid @RequestBody AppointmentDTO appointmentDTO) throws URISyntaxException {
        log.debug("REST request to save Appointment : {}", appointmentDTO);
        Appointment appointment = appointmentService.create(appointmentMapper.appointmentDTOToAppointment(appointmentDTO));
        AppointmentDTO result = appointmentMapper.appointmentToAppointmentDTO(appointment);
        return ResponseEntity.created(new URI("/api/appointments/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("appointment", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /appointments -> Updates an existing appointment.
     */
    @RequestMapping(value = "/appointments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AppointmentDTO> update(@Valid @RequestBody AppointmentDTO appointmentDTO) throws URISyntaxException {
        log.debug("REST request to update Appointment : {}", appointmentDTO);
        Appointment appointment = appointmentService.update(appointmentMapper.appointmentDTOToAppointment(appointmentDTO));
        AppointmentDTO result = appointmentMapper.appointmentToAppointmentDTO(appointment);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("appointment", result.getId().toString()))
                .body(result);
    }

    /**
     * GET  /appointments -> get all the appointments.
     */
    @RequestMapping(value = "/appointments",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<AppointmentDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Appointment> page = appointmentService.getAll(offset, limit);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/appointments", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
                .map(appointmentMapper::appointmentToAppointmentDTO)
                .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /appointments/:id -> get the "id" appointment.
     */
    @RequestMapping(value = "/appointments/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AppointmentDTO> get(@PathVariable Long id) {
        log.debug("REST request to get Appointment : {}", id);
        return Optional.ofNullable(appointmentService.get(id))
        		.map(appointmentMapper::appointmentToAppointmentDTO)
            .map(appointment -> new ResponseEntity<>(
                appointment,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /appointments/:id -> delete the "id" appointment.
     */
    @RequestMapping(value = "/appointments/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Appointment : {}", id);
        appointmentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("appointment", id.toString())).build();
    }

    /**
     * SEARCH  /_search/appointments/:query -> search for the appointment corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/appointments/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AppointmentDTO> search(@PathVariable String query) {
        return StreamSupport
            .stream(appointmentService.search(query).spliterator(), false)
            .map(appointmentMapper::appointmentToAppointmentDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * GET  /appointmentsListForUser -> get all the appointments for the current user.
     */
    @RequestMapping(value = "/appointmentsListForUser",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AppointmentDTO> getForUser(){
    	return StreamSupport
    			.stream(appointmentService.getForUser().spliterator(), false)
    			.map(appointmentMapper::appointmentToAppointmentDTO)
    			.collect(Collectors.toList());
    }
    
    /**
     * GET  /appointmentsListForUser -> get all the appointments for the current user.
     */
    @RequestMapping(value = "/appointmentsListForExpert",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AppointmentDTO> getForExpert(){
        return StreamSupport
        		.stream(appointmentService.getForExpert().spliterator(), false)
        		.map(appointmentMapper::appointmentToAppointmentDTO)
        		.collect(Collectors.toList());
    }
    
    
    /**
     * SEARCH  /_search/expertFieldsActive/:query -> search for the expertField corresponding
     * to the query.
     */
    @RequestMapping(value="/_search/appointmentsListForUser/{query}",
    		method = RequestMethod.GET,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AppointmentDTO> searchInUsersAppointments(@PathVariable String query){
    	return StreamSupport
	            .stream(appointmentService.searchInUsersAppointments(query).spliterator(), false)
	            .map(appointmentMapper::appointmentToAppointmentDTO)
	            .collect(Collectors.toList());
    }
    
    /**
     * SEARCH  /_search/expertFieldsActive/:query -> search for the expertField corresponding
     * to the query.
     */
    @RequestMapping(value="/_search/appointmentsListForExpert/{query}",
    		method = RequestMethod.GET,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AppointmentDTO> searchInExpertsAppointments(@PathVariable String query){
    	return StreamSupport
	            .stream(appointmentService.searchInExpertsAppointments(query).spliterator(), false)
	            .map(appointmentMapper::appointmentToAppointmentDTO)
	            .collect(Collectors.toList());
    }

}
