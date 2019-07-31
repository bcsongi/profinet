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

import edu.codespring.profinet.domain.Keyword;
import edu.codespring.profinet.repository.KeywordRepository;
import edu.codespring.profinet.repository.search.KeywordSearchRepository;
import edu.codespring.profinet.web.rest.util.PaginationUtil;

@Service
@Transactional
public class KeywordService {

    private final Logger log = LoggerFactory.getLogger(KeywordService.class);

    @Inject
    private KeywordRepository keywordRepository;

    @Inject
    private KeywordSearchRepository keywordSearchRepository;
    
    /**
     * Create a new keyword.
     */
    public Keyword create(Keyword keyword) throws ServiceException {
        log.debug("Service method to save Keyword : {}", keyword);
        if (keyword.getId() != null) {
            throw new ServiceException("Failure \n A new keyword cannot already have an ID");
        }
        Keyword result = keywordRepository.save(keyword);
        keywordSearchRepository.save(result);
        return result;
    }

    /**
     * Updates an existing keyword.
     */
    public Keyword update(Keyword keyword) {
        log.debug("Service method to update Keyword : {}", keyword);
        if (keyword.getId() == null) {
            return create(keyword);
        }
        Keyword result = keywordRepository.save(keyword);
        keywordSearchRepository.save(keyword);
        return result;
    }

    /**
     * Get all the keywords.
     */
    public Page<Keyword> getAll(Integer offset, Integer limit){
        Page<Keyword> page = keywordRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        return page;
    }

    /**
     * Get the "id" keyword.
     */
    public Keyword get(Long id) {
        log.debug("Service method to get Keyword : {}", id);
        return keywordRepository.findOne(id);
    }

    /**
     * Delete the "id" keyword.
     */
    public void delete(Long id) {
        log.debug("Service method to delete Keyword : {}", id);
        keywordRepository.delete(id);
        keywordSearchRepository.delete(id);
    }

    /**
     * Search for the keyword corresponding to the query.
     */
    public List<Keyword> search(String query) {
        return StreamSupport
            .stream(keywordSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
    
    
    /**
     * Get the "id" keyword.
     */
    public List<Keyword> getByName(String name) {
        return  StreamSupport
                .stream(keywordSearchRepository.findByNameLike(name).spliterator(), false)
                .collect(Collectors.toList());
    }
}
