package edu.codespring.profinet.repository.search;

import edu.codespring.profinet.domain.Language;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Language entity.
 */
public interface LanguageSearchRepository extends ElasticsearchRepository<Language, Long> {
}
