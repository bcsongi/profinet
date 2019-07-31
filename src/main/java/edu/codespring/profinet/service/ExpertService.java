package edu.codespring.profinet.service;

import static org.elasticsearch.index.query.QueryBuilders.queryString;

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
import edu.codespring.profinet.domain.Expert;
import edu.codespring.profinet.domain.ExpertField;
import edu.codespring.profinet.domain.Keyword;
import edu.codespring.profinet.repository.ExpertRepository;
import edu.codespring.profinet.repository.KeywordRepository;
import edu.codespring.profinet.repository.search.ExpertFieldSearchRepository;
import edu.codespring.profinet.repository.search.ExpertSearchRepository;
import edu.codespring.profinet.repository.search.KeywordSearchRepository;
import edu.codespring.profinet.web.rest.util.PaginationUtil;

@Service
@Transactional
public class ExpertService {

    private final Logger log = LoggerFactory.getLogger(ExpertService.class);
    
    @Inject
    private ExpertRepository expertRepository;

    @Inject
    private ExpertSearchRepository expertSearchRepository;
    
    @Inject
    private ExpertFieldSearchRepository expertFieldSearchRepository;
    
    @Inject
    private ExpertFieldService expertFieldService;

    @Inject
    private KeywordRepository keywordRepository;

    @Inject
    private KeywordSearchRepository keywordSearchRepository;

    /**
     * Create a new expert.
     */
    public Expert create(Expert expert) throws ServiceException {
        log.debug("Service method to save Expert : {}", expert);
        if (expert.getId() != null) {
            throw new ServiceException("Failure \n A new expert cannot already have an ID");        }
       
        for (Keyword k : expert.getExpertkeywords()) {
        	if (k.getId() == null) {
    			Keyword result = keywordRepository.save(k);
    			keywordSearchRepository.save(result);
    		}
    	}
        
        Expert result = expertRepository.save(expert);
        expertSearchRepository.save(expertRepository.findOne(expert.getId())); 
        return result;
    }

    /**
     * Updates an existing expert.
     */
    public Expert update(Expert expert){
        log.debug("Service method to update Expert : {}", expert);
        if (expert.getId() == null) {
        	return create(expert);
        }
        
        for (Keyword k : expert.getExpertkeywords()) {
        	if (k.getId() == null) {
    			Keyword result = keywordRepository.save(k);
    			keywordSearchRepository.save(result);
    		}
    	}
        
        Expert result = expertRepository.save(expert);
        expertSearchRepository.save(expertRepository.findOne(expert.getId())); 
        List<ExpertField> expertFields = expertFieldSearchRepository.findByExpert_id(expert.getId());
        for (ExpertField expertField : expertFields){
        	expertField.setExpert(expert);
        	expertFieldService.update(expertField);
        }
        return result;
    }

    /**
     * Get all the experts.
     */
    public Page<Expert> getAll(Integer offset, Integer limit) throws ServiceException {
        Page<Expert> page = expertRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        return page;
    }		
    		
    /**
     * Get all the experts.
     */
    public Page<Expert> getAllFromIndex(Integer offset, Integer limit) throws ServiceException {
        Page<Expert> page = expertSearchRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        return page;
    }
    
    /**
     * Get the "id" expert.
     */
    public Expert get(String login) {
        log.debug("Service method to get Expert : {} ", login);
        return expertRepository.findByLogin(login);
    }
    
    /**
     * Get the "id" expert.
     */
    public Expert expertUpdateLastDate(Long id) {
    	log.debug("Service method to get expertUpdateLastDate id : {} ", id);
    	
    	java.util.Date utilDate = new java.util.Date();
    	java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        
    	Expert expert = expertRepository.findOne(id);
    	expert.setLastActive(new DateTime(sqlDate));
    	
        Expert result = expertRepository.save(expert);
        expertSearchRepository.save(result); 
        
        return result;
    }

    /**
     * Delete the "id" expert.
     */
    public void delete(Long id) {
        log.debug("Service method to delete Expert : {}", id);
        expertRepository.delete(id);
        expertSearchRepository.delete(id);
    }

    /**
     * Search for the expert corresponding to the query.
     */
    public List<Expert> search(@PathVariable String query) {
        return StreamSupport
            .stream(expertSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
    
    /**
     * Get the "id" expert.
     */
    public Expert getById(Long id) {
        log.debug("Service method to get Expert : {} ", id);
        return expertRepository.findOne(id);
    }
    
}
