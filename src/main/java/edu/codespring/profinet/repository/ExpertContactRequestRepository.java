package edu.codespring.profinet.repository;

import edu.codespring.profinet.domain.ExpertContactRequest;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ExpertContactRequest entity.
 */
public interface ExpertContactRequestRepository extends JpaRepository<ExpertContactRequest,Long> {

    @Query("select expertContactRequest from ExpertContactRequest expertContactRequest where expertContactRequest.userRequest.login = ?#{principal.username}")
    List<ExpertContactRequest> findAllForCurrentUser();

}
