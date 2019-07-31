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
import org.springframework.web.bind.annotation.RequestBody;
import edu.codespring.profinet.domain.Appointment;
import edu.codespring.profinet.domain.ExpertField;
import edu.codespring.profinet.repository.ExpertFieldRepository;
import edu.codespring.profinet.repository.search.AppointmentSearchRepository;
import edu.codespring.profinet.repository.search.ExpertFieldSearchRepository;
import edu.codespring.profinet.web.rest.util.PaginationUtil;

@Service
@Transactional
public class ExpertFieldService {
	 private final Logger log = LoggerFactory.getLogger(ExpertFieldService.class);

	    @Inject
	    private ExpertFieldRepository expertFieldRepository;

	    @Inject
	    private ExpertFieldSearchRepository expertFieldSearchRepository;
	    
	    @Inject
	    private AppointmentSearchRepository appointmentSearchRepository;
	    
	    @Inject
	    private AppointmentService appointmentService;
	    
	    /**
	     * Create a new expertField.
	     */
	    public ExpertField create(@RequestBody ExpertField expertField) throws ServiceException {
	        log.debug("Service method to save ExpertField : {}", expertField);
	        if (expertField.getId() != null) {
	            throw new ServiceException("Failure \n A new expertField cannot already have an ID");
	        }
	        ExpertField result = expertFieldRepository.save(expertField);
	        expertFieldSearchRepository.save(expertFieldRepository.findOne(expertField.getId()));
	        return result;
	    }

	    /**
	     * Updates an existing expertField.
	     */
	    public ExpertField update(ExpertField expertField) throws ServiceException {
	        log.debug("REST request to update ExpertField : {}", expertField);
	        if (expertField.getId() == null) {
	            return create(expertField);
	        }
	        ExpertField result = expertFieldRepository.save(expertField);
	        expertFieldSearchRepository.save(expertFieldRepository.findOne(expertField.getId()));
	        
	        List<Appointment> appointments = appointmentSearchRepository.findByExpertField_id(expertField.getId());
	        for (Appointment appointment: appointments){
	        	appointment.setExpertField(expertField);
	        	appointmentService.update(appointment);
	        }
	        
	        return result;
	    }

	    /**
	     * Get all the expertFields.
	     */
	    public Page<ExpertField> getAll(Integer offset, Integer limit) throws ServiceException {
	        Page<ExpertField> page = expertFieldRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
	        return page;
	    }
	    
	    /**
	     * Get all the expertFields from index.
	     */
	    public Page<ExpertField> getAllFromIndex(Integer offset, Integer limit) throws ServiceException {
	        Page<ExpertField> page = expertFieldSearchRepository.findByExpert_Active(true, PaginationUtil.generatePageRequest(offset, limit));
	        return page;
	    }

	    /**
	     * Get the "id" expertField.
	     */
	    public ExpertField get(Long id) {
	        log.debug("Service method to get ExpertField : {}", id);
	        return expertFieldRepository.findOne(id);
	    }

	    /**
	     * Delete the "id" expertField.
	     */
	    public void delete(Long id) {
	        log.debug("Service method to delete ExpertField : {}", id);
	        expertFieldRepository.delete(id);
	        expertFieldSearchRepository.delete(id);
	    }

	    /**
	     * Search for the expertField corresponding to the query.
	     */
	    public List<ExpertField> search(String query) {
	        return StreamSupport
	            .stream(expertFieldSearchRepository.search(queryString(query)).spliterator(), false)
	            .collect(Collectors.toList());
	    }
	    
	    /**
	     * Search for the expertField corresponding
	     * to the query.
	     */
	    public List<ExpertField> searchActives(String query) {
	    	////THIS HAS TO BE MODIFIED!!!!
	    	
	        return StreamSupport
	            .stream(expertFieldSearchRepository.search(queryString(query)).spliterator(), false)
	            .collect(Collectors.toList());
	    }
	    
	    /**
	     * Search for the expertField corresponding
	     * to the query.
	     */
	    public List<ExpertField> filterByCategory(String category) {
	        return StreamSupport
	            .stream(expertFieldSearchRepository.findByField_NameAndExpert_ActiveOrderByRatingDesc(category, true).spliterator(), false)
	            .collect(Collectors.toList());
	    }
	    
	    /**
	     * Search for the expertField corresponding
	     * to the query.
	     */
	    public List<ExpertField> filterByLanguage(String language) {
	        return StreamSupport
	            .stream(expertFieldSearchRepository.findByExpert_Expertlanguages_NameAndExpert_ActiveOrderByRatingDesc(language, true).spliterator(), false)
	            .collect(Collectors.toList());
	    }
	    
	    /**
	     * Search for the expertField corresponding
	     * to the query.
	     */
	    public List<ExpertField> filterByCategoryAndLanguage(String category,String language) {
	        return StreamSupport
	            .stream(expertFieldSearchRepository.findByField_NameAndExpert_Expertlanguages_NameAndExpert_ActiveOrderByRatingDesc(category, language, true).spliterator(), false)
	            .collect(Collectors.toList());
	    }
	    
	    /**
	     * Search for the expertField corresponding
	     * to the query.
	     */
	    public List<ExpertField> expertFieldsByRating() {
	        return StreamSupport
	            .stream(expertFieldSearchRepository.findByExpert_ActiveOrderByRatingDesc(true).spliterator(), false)
	            .collect(Collectors.toList());
	    }
	    
	    /**
	     * Search for the expertField corresponding
	     * to the query.
	     */
	    public List<ExpertField> searchInActiveExperts(String query){
	    	return StreamSupport
		            .stream(expertFieldSearchRepository.findInActiveExperts(query).spliterator(), false)
		            .collect(Collectors.toList());
	    }
	    
	    /**
	     * Get all the expertFields from index.
	     */
	    public List<ExpertField> expertFieldsByUserLogin(String login) {	
	    	return expertFieldRepository.findOneByLogin(login);
	    }
	    
	}
