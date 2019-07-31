package edu.codespring.profinet.service;

import static org.elasticsearch.index.query.QueryBuilders.queryString;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.codespring.profinet.domain.ExpertField;
import edu.codespring.profinet.domain.Field;
import edu.codespring.profinet.repository.ExpertFieldRepository;
import edu.codespring.profinet.repository.FieldRepository;
import edu.codespring.profinet.repository.search.FieldSearchRepository;
import edu.codespring.profinet.web.rest.util.PaginationUtil;

@Service
@Transactional
public class FieldService {

    private final Logger log = LoggerFactory.getLogger(FieldService.class);

    @Inject
    private FieldRepository fieldRepository;

    @Inject
    private FieldSearchRepository fieldSearchRepository;

    @Inject
    private ExpertFieldRepository expertFieldRepository; 
	
    /**
     * Create a new field.
     */
    public Field create(Field field) throws ServiceException {
        log.debug("Service method to save Field : {}", field);
        if (field.getId() != null) {
            throw new ServiceException("Failure \n A new field cannot already have an ID");
        }
        Field result = fieldRepository.save(field);
        fieldSearchRepository.save(result);
        return result;
    }

    /**
     * Updates an existing field.
     */
    public Field update(Field field) {
        log.debug("Service method to update Field : {}", field);
        if (field.getId() == null) {
            return create(field);
        }
        Field result = fieldRepository.save(field);
        fieldSearchRepository.save(field);
        return result;
    }

    /**
     * Get all the fields.
     */
    public Page<Field> getAll(Integer offset, Integer limit) {
        Page<Field> page = fieldRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        return page;
    }

    /**
     * Get the "id" field.
     */
    public Field get(Long id) {
        log.debug("Service method to get Field : {}", id);
        return fieldRepository.findOne(id);
    }

    /**
     * Get the "id" field.
     */
    public List<Field> fieldsByExpertId(Long id) {
    	List<Field> fields = fieldRepository.findAll();
    	List<ExpertField> expertFields  = expertFieldRepository.findAllForCurrentUser();
	
    	HashMap<Integer, Field> map = new HashMap<Integer, Field>();
    	for (int i = 0; i < fields.size(); i++) {
    		map.put(fields.get(i).getId().intValue(), null);
    	}
    	
    	for (ExpertField ef : expertFields) {
    		for (Field f : fields) {
    			if (ef.getField().getName().equals(f.getName())) {
    				map.put(f.getId().intValue(), f);
    			}
    		}
    	}
    		
    	for (Entry<Integer, Field> entry : map.entrySet()) { 
    		fields.remove(entry.getValue());
    	}
    	
    	return fields;
    }    
    
    /**
     * Delete the "id" field.
     */
    public void delete(Long id) {
        log.debug("Service method to delete Field : {}", id);
        fieldRepository.delete(id);
        fieldSearchRepository.delete(id);
    }

    /**
     * Search for the field corresponding to the query.
     */
    public List<Field> search(String query) {
        return StreamSupport
            .stream(fieldSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
