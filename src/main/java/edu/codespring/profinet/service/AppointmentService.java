package edu.codespring.profinet.service;

import static org.elasticsearch.index.query.QueryBuilders.queryString;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.codespring.profinet.domain.Appointment;
import edu.codespring.profinet.repository.AppointmentRepository;
import edu.codespring.profinet.repository.search.AppointmentSearchRepository;
import edu.codespring.profinet.web.rest.util.PaginationUtil;
import edu.codespring.profinet.service.ServiceException;

@Service
@Transactional
public class AppointmentService {

    private final Logger log = LoggerFactory.getLogger(AppointmentService.class);

    @Inject
    private AppointmentRepository appointmentRepository;

    @Inject
    private AppointmentSearchRepository appointmentSearchRepository;
    
    /**
     * Create a new appointment.
     */
    public Appointment create(Appointment appointment) throws ServiceException{
        log.debug("Service method to save Appointment : {}", appointment);
        if (appointment.getId() != null) {
            throw new ServiceException("Appointment already has an ID");
        }
        Appointment result = appointmentRepository.save(appointment);
        appointmentSearchRepository.save(result);
        return result; 
    }

    /**
     * Updates an existing appointment.
     */
    public Appointment update(Appointment appointment){
        log.debug("Service method to update Appointment : {}", appointment);
        if (appointment.getId() == null) {
            return create(appointment);
        }
        Appointment result = appointmentRepository.save(appointment);
        appointmentSearchRepository.save(appointment);
        return result;
    }

    /**
     * Get all the appointments.
     */
    public Page<Appointment> getAll(Integer offset, Integer limit){
        return appointmentRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
    }

    /**
     * Get the "id" appointment.
     */
    public Appointment get(Long id) {
        log.debug("Service method to get Appointment : {}", id);
        return appointmentRepository.findOne(id);
    }

    /**
     * Delete the "id" appointment.
     */
    public void delete(Long id) {
        log.debug("Service method to delete Appointment : {}", id);
        appointmentRepository.delete(id);
        appointmentSearchRepository.delete(id);
    }

    /**
     * Search for the appointment corresponding
     * to the query.
     */
    public List<Appointment> search(String query) {
        return StreamSupport
            .stream(appointmentSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
    
    /**
     * Get all the appointments for the current user.
     */
    public List<Appointment> getForUser(){
        return appointmentRepository.findAllForCurrentUser();
    }
    
    /**
     * Get all the appointments for the current user.
     */
    public List<Appointment> getForExpert(){
        return appointmentRepository.findAllForCurrentExpert();
    }
    
    
    /**
     * Search for the expertField corresponding
     * to the query.
     */
    public List<Appointment> searchInUsersAppointments(String query){
    	return StreamSupport
	            .stream(appointmentSearchRepository.findInUsersAppointment(query).spliterator(), false)
	            .collect(Collectors.toList());
    }
    
    /**
     * Search for the expertField corresponding
     * to the query.
     */
    public List<Appointment> searchInExpertsAppointments(String query){
    	return StreamSupport
	            .stream(appointmentSearchRepository.findInExpertsAppointment(query).spliterator(), false)
	            .collect(Collectors.toList());
    }


}
