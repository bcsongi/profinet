package edu.codespring.profinet.repository.search;

import java.util.List;

import edu.codespring.profinet.domain.ExpertField;
import edu.codespring.profinet.domain.Field;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Field entity.
 */
public interface FieldSearchRepository extends ElasticsearchRepository<Field, Long> {
}
