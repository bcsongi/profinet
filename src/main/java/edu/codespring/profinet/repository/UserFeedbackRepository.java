package edu.codespring.profinet.repository;

import edu.codespring.profinet.domain.Expert;
import edu.codespring.profinet.domain.ExpertField;
import edu.codespring.profinet.domain.UserFeedback;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the UserFeedback entity.
 */
public interface UserFeedbackRepository extends JpaRepository<UserFeedback,Long> {

	/*	
    @Query("select expert from Expert expert left join fetch expert.expertkeywords left join fetch expert.expertlanguages where expert.id =:id")
    Expert findOneWithEagerRelationships(@Param("id") Long id);
    */
	
	@Query("select userFeedback from UserFeedback userFeedback where appointmentuserfeedback.id =:id")
	UserFeedback findOneByAppointmentId(@Param("id") Long id);
	
}
