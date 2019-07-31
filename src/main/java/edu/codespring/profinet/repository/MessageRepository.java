package edu.codespring.profinet.repository;

import edu.codespring.profinet.domain.Message;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Message entity.
 */
public interface MessageRepository extends JpaRepository<Message,Long> {

    @Query("select message from Message message where message.userfrom.login = ?#{principal.username}")
    List<Message> findAllForCurrentFromUser();

    @Query("select message from Message message where message.userto.login = ?#{principal.username}")
    List<Message> findAllForCurrentToUser();

}
