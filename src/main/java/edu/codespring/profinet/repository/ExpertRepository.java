package edu.codespring.profinet.repository;

import edu.codespring.profinet.domain.Expert;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Expert entity.
 */
public interface ExpertRepository extends JpaRepository<Expert,Long> {

    @Query("select expert from Expert expert left join fetch expert.expertkeywords left join fetch expert.expertlanguages where expert.id =:id")
    Expert findOneWithEagerRelationships(@Param("id") Long id);
    
    @Query("select expert from Expert expert left join fetch expert.expertkeywords left join expert.expertuser u left join fetch expert.expertlanguages where u.login=:login")
    Expert findByLogin(@Param("login") String login);

}
