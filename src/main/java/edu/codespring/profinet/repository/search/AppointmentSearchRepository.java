package edu.codespring.profinet.repository.search;

import edu.codespring.profinet.domain.Appointment;
import edu.codespring.profinet.domain.ExpertField;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Appointment entity.
 */
public interface AppointmentSearchRepository extends ElasticsearchRepository<Appointment, Long>,AppointmentSearchRepositoryCustom {
	List<Appointment> findByExpertField_id(Long id);	
}
