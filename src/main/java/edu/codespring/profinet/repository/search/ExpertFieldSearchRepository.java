package edu.codespring.profinet.repository.search;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import edu.codespring.profinet.domain.ExpertField;


/**
 * Spring Data ElasticSearch repository for the ExpertField entity.
 */

public interface ExpertFieldSearchRepository extends ElasticsearchRepository<ExpertField, Long>, ExpertFieldSearchRepositoryCustom {

	List<ExpertField> findByField_NameAndExpert_ActiveOrderByRatingDesc(String field, Boolean active);
	
	List<ExpertField> findByExpert_Expertlanguages_NameAndExpert_ActiveOrderByRatingDesc(String language, Boolean active);
	
	List<ExpertField> findByField_NameAndExpert_Expertlanguages_NameAndExpert_ActiveOrderByRatingDesc(String field, String language, Boolean active);

	List<ExpertField> findByExpert_ActiveOrderByRatingDesc(Boolean active);
	
	List<ExpertField> findByExpert_id(Long id);
	
	List<ExpertField> findByExpert_Expertuser_LoginAndExpert_Active(String login, Boolean active);
	
	Page<ExpertField> findByExpert_Active(Boolean active, Pageable pageable);
	
}
