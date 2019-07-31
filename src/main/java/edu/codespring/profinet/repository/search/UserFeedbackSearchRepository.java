package edu.codespring.profinet.repository.search;

import java.util.List;

import edu.codespring.profinet.domain.UserFeedback;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data ElasticSearch repository for the UserFeedback entity.
 */
public interface UserFeedbackSearchRepository extends ElasticsearchRepository<UserFeedback, Long> {
	
	List<UserFeedback> findByAppointmentuserfeedback_id(Long id);

	List<UserFeedback> findByAppointmentuserfeedback_ExpertField_id(Long id);	
	
}
