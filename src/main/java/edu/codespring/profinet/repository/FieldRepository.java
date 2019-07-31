package edu.codespring.profinet.repository;

import edu.codespring.profinet.domain.Field;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Field entity.
 */
public interface FieldRepository extends JpaRepository<Field,Long> {

}
