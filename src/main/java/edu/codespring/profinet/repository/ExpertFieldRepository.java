package edu.codespring.profinet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.codespring.profinet.domain.Expert;
import edu.codespring.profinet.domain.ExpertContactRequest;
import edu.codespring.profinet.domain.ExpertField;

public interface ExpertFieldRepository extends JpaRepository<ExpertField, Long> {

	@Query("select expertField from ExpertField expertField where expertField.expert.expertuser.login = ?#{principal.username}")
    List<ExpertField> findAllForCurrentUser();

	@Query("select expertField from ExpertField expertField order by expertField.rating desc")
    List<ExpertField> findByRating();
	
	@Query("select expertField from ExpertField expertField where expertField.expert.expertuser.login=:login order by expertField.rating desc")
	List<ExpertField> findOneByLogin(@Param("login") String login);
	
}
