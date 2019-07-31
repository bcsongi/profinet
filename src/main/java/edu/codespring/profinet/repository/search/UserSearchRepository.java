package edu.codespring.profinet.repository.search;

import edu.codespring.profinet.domain.User;

import java.util.Optional;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the User entity.
 */
public interface UserSearchRepository extends ElasticsearchRepository<User, Long> {
	
	Optional<User> findOneByLogin(String login);
}
