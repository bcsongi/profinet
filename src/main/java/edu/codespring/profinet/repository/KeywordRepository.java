package edu.codespring.profinet.repository;

import edu.codespring.profinet.domain.Keyword;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Keyword entity.
 */
public interface KeywordRepository extends JpaRepository<Keyword,Long> {

}
