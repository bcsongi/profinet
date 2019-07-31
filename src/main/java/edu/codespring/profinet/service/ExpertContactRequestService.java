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
import org.springframework.web.bind.annotation.PathVariable;
import edu.codespring.profinet.domain.ExpertContactRequest;
import edu.codespring.profinet.repository.ExpertContactRequestRepository;
import edu.codespring.profinet.repository.search.ExpertContactRequestSearchRepository;
import edu.codespring.profinet.web.rest.util.PaginationUtil;

@Service
@Transactional
public class ExpertContactRequestService {

    private final Logger log = LoggerFactory.getLogger(ExpertContactRequestService.class);

    @Inject
    private ExpertContactRequestRepository expertContactRequestRepository;

    @Inject
    private ExpertContactRequestSearchRepository expertContactRequestSearchRepository;

    /**
     * Create a new expertContactRequest.
     */
    public ExpertContactRequest create(ExpertContactRequest expertContactRequest) throws ServiceException{
        log.debug("Service to save ExpertContactRequest : {}", expertContactRequest);
        if (expertContactRequest.getId() != null) {
            throw new ServiceException("ExpertContactRequest already has an ID");
        }
        ExpertContactRequest result = expertContactRequestRepository.save(expertContactRequest);
        expertContactRequestSearchRepository.save(expertContactRequestRepository.findOne(expertContactRequest.getId()));
        return result;
    }

    /**
     * Updates an existing expertContactRequest.
     */
    public ExpertContactRequest update(ExpertContactRequest expertContactRequest) throws ServiceException {
        log.debug("Service method to update ExpertContactRequest : {}", expertContactRequest);
        if (expertContactRequest.getId() == null) {
            return create(expertContactRequest);
        }
        ExpertContactRequest result = expertContactRequestRepository.save(expertContactRequest);
        expertContactRequestSearchRepository.save(expertContactRequest);
        return result;
    }

    /**
     * Get all the expertContactRequests.
     */
    public Page<ExpertContactRequest> getAll(Integer offset, Integer limit){
        return expertContactRequestRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
    }

    /**
     * Get the "id" expertContactRequest.
     */
    public ExpertContactRequest get(Long id) {
        log.debug("Service to get ExpertContactRequest : {}", id);
        return expertContactRequestRepository.findOne(id);
    }

    /**
     * Delete the "id" expertContactRequest.
     */
    public void delete(Long id) {
        log.debug("Service method to delete ExpertContactRequest : {}", id);
        expertContactRequestRepository.delete(id);
        expertContactRequestSearchRepository.delete(id);
    }

    /**
     * Search for the expertContactRequest corresponding
     * to the query.
     */
    public List<ExpertContactRequest> search(@PathVariable String query) {
        return StreamSupport
            .stream(expertContactRequestSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
    
    /**
     * Search for the expertContactRequest corresponding
     * to the query.
     */
    public List<ExpertContactRequest> getStatusBetween(Long expert, String user) {
        log.debug("Service method to get ExpertContactRequest : expert: {} user: {}", expert, user);
        return StreamSupport
                .stream(expertContactRequestSearchRepository.findByUserRequest_loginAndExpertrequest_id(user,expert).spliterator(), false)
                .collect(Collectors.toList());
    }
    
    /**
     * Get the "id" expertContactRequests.
     */
    public Page<ExpertContactRequest> getExpertContactRequestsTo(Integer offset, Integer limit, Long id){
        Page<ExpertContactRequest> page = expertContactRequestSearchRepository.findByExpertrequest_id(id, PaginationUtil.generatePageRequest(offset, limit));
        return page;
    }
}