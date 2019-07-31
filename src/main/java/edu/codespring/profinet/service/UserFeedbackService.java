package edu.codespring.profinet.service;

import static org.elasticsearch.index.query.QueryBuilders.queryString;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import edu.codespring.profinet.domain.Appointment;
import edu.codespring.profinet.domain.ExpertField;
import edu.codespring.profinet.domain.UserFeedback;
import edu.codespring.profinet.repository.AppointmentRepository;
import edu.codespring.profinet.repository.ExpertFieldRepository;
import edu.codespring.profinet.repository.UserFeedbackRepository;
import edu.codespring.profinet.repository.search.AppointmentSearchRepository;
import edu.codespring.profinet.repository.search.ExpertFieldSearchRepository;
import edu.codespring.profinet.repository.search.UserFeedbackSearchRepository;
import edu.codespring.profinet.web.rest.util.PaginationUtil;

@Service
@Transactional
public class UserFeedbackService {

    private final Logger log = LoggerFactory.getLogger(UserFeedbackService.class);

    @Inject
    private UserFeedbackRepository userFeedbackRepository;

    @Inject
    private UserFeedbackSearchRepository userFeedbackSearchRepository;
    
    @Inject
    private AppointmentRepository appointmentRepository;
    
    @Inject
    private AppointmentSearchRepository appointmentSearchRepository;
    
    @Inject
    private ExpertFieldRepository expertFieldRepository;
    
    @Inject
    private ExpertFieldSearchRepository expertFieldSearchRepository;
    
    /**
     * Create a new userFeedback.
     */
    public UserFeedback create(UserFeedback userFeedback) throws ServiceException {
        log.debug("Service method to save UserFeedback : {}", userFeedback);
        if (userFeedback.getId() != null) {
            throw new ServiceException("Failure \n A new userFeedback cannot already have an ID");
        }
        UserFeedback result = userFeedbackRepository.save(userFeedback);
        userFeedbackSearchRepository.save(result);
        return result;
    }

    /**
     * Updates an existing userFeedback.
     */
    public UserFeedback update(UserFeedback userFeedback) {
        log.debug("Service method to update UserFeedback : {}", userFeedback);
        if (userFeedback.getId() == null) {
            return create(userFeedback);
        }
        UserFeedback result = userFeedbackRepository.save(userFeedback);
        userFeedbackSearchRepository.save(userFeedback);
        return result;
    }

    /**
     * Get all the userFeedbacks.
     */
    public Page<UserFeedback> getAll(Integer offset, Integer limit){
        Page<UserFeedback> page = userFeedbackRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        return page;
    }

    /**
     * Get the "id" userFeedback.
     */
    public UserFeedback get(Long id) {
        log.debug("Service method to get UserFeedback : {}", id);
        return userFeedbackRepository.findOne(id);
    }
    
    /**
     * Get the "id" userFeedback.
     */
    public UserFeedback getUserFeedbackByAppointment(Long id) {
    	return userFeedbackRepository.findOneByAppointmentId(id);
    }

    /**
     * Delete the "id" userFeedback.
     */
    public void delete(Long id) {
        log.debug("Service method to delete UserFeedback : {}", id);
        userFeedbackRepository.delete(id);
        userFeedbackSearchRepository.delete(id);
    }

    /**
     * Search for the userFeedback corresponding to the query.
     */
    public List<UserFeedback> search(String query) {
        return StreamSupport
            .stream(userFeedbackSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
    
    /**
     * Search for the userFeedback corresponding to the query.
     */
    public List<UserFeedback> searchByExpertField(@PathVariable Long expertFieldId) {
        return StreamSupport
            .stream(userFeedbackSearchRepository.findByAppointmentuserfeedback_ExpertField_id(expertFieldId).spliterator(), false)
            .collect(Collectors.toList());
    }

    /**
     * Set the "id" userFeedback. 
     */
    public List<UserFeedback> setExpertFieldByUserFeedback(Integer rating, String comment, Long appointmentid) {
    	Calendar cal = Calendar.getInstance();
    	
    	UserFeedback userFeedback = new UserFeedback();
    	userFeedback.setRating(rating);
    	userFeedback.setComment(comment);
    	
    	userFeedback.setDate(new DateTime(cal.getTime()));
    	
    	Appointment appointment = appointmentRepository.findOne(appointmentid);
    
    	userFeedback.setAppointmentuserfeedback(appointment);

    	UserFeedback result1 = userFeedbackRepository.save(userFeedback);
    	userFeedbackSearchRepository.save(result1);
    	
    	Integer nrOfUserfeedback = 0;
    	Integer sumOfUserfeedback = 0;
    	List<UserFeedback> allUserFeedback = userFeedbackSearchRepository.findByAppointmentuserfeedback_id(appointmentid);
    	
    	for (UserFeedback uf : allUserFeedback) {
    		if (uf.getRating() != null) {
	    		nrOfUserfeedback = nrOfUserfeedback + 1;
				sumOfUserfeedback += uf.getRating();
    		}
    	} 
    	
    	ExpertField expertfield = appointment.getExpertField();
    	expertfield.setRating(sumOfUserfeedback / (double) nrOfUserfeedback);
    	
    	ExpertField result2 = expertFieldRepository.save(expertfield);
    	expertFieldSearchRepository.save(result2);
    	
    	Appointment appointment2 = appointment;
    	appointment2.setRated(true);
    	Appointment result3 = appointmentRepository.save(appointment2);
    	appointmentSearchRepository.save(result3);
    	
    	return allUserFeedback;
    }
}

