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
import edu.codespring.profinet.domain.Language;
import edu.codespring.profinet.repository.LanguageRepository;
import edu.codespring.profinet.repository.search.LanguageSearchRepository;
import edu.codespring.profinet.web.rest.util.PaginationUtil;

@Service
@Transactional
public class LanguageService {

    private final Logger log = LoggerFactory.getLogger(LanguageService.class);

    @Inject
    private LanguageRepository languageRepository;

    @Inject
    private LanguageSearchRepository languageSearchRepository;

    /**
     * Create a new language.
     */
    public Language create(Language language) throws ServiceException {
        log.debug("Service method to save Language : {}", language);
        if (language.getId() != null) {
            throw new ServiceException("Failure \n A new language cannot already have an ID");
        }
        Language result = languageRepository.save(language);
        languageSearchRepository.save(result);
        return result;
    }

    /**
     * Updates an existing language.
     */
    public Language update(Language language) {
        log.debug("Service method to update Language : {}", language);
        if (language.getId() == null) {
            return create(language);
        }
        Language result = languageRepository.save(language);
        languageSearchRepository.save(language);
        return result;
    }

    /**
     * Get all the languages.
     */
    public Page<Language> getAll(Integer offset, Integer limit){
        Page<Language> page = languageRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        return page;
    }

    /**
     * Get the "id" language.
     */
    public Language get(Long id) {
        log.debug("Service methodt to get Language : {}", id);
        return languageRepository.findOne(id);
    }

    /**
     * Delete the "id" language.
     */
    public void delete( Long id) {
        log.debug("Service method to delete Language : {}", id);
        languageRepository.delete(id);
        languageSearchRepository.delete(id);
    }

    /**
     * Search for the language corresponding to the query.
     */
    public List<Language> search(String query) {
        return StreamSupport
            .stream(languageSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

