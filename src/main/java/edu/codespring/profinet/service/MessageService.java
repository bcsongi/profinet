package edu.codespring.profinet.service;

import static org.elasticsearch.index.query.QueryBuilders.queryString;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.codespring.profinet.domain.Message;
import edu.codespring.profinet.repository.MessageRepository;
import edu.codespring.profinet.repository.search.MessageSearchRepository;
import edu.codespring.profinet.web.rest.util.PaginationUtil;

@Service
@Transactional
public class MessageService {
	private final Logger log = LoggerFactory.getLogger(MessageService.class);

    @Inject
    private MessageRepository messageRepository;

    @Inject
    private MessageSearchRepository messageSearchRepository;

    /**
     * Create a new message.
     */
    public Message create(Message message) throws ServiceException {
        log.debug("Service method to save Message : {}", message);
        if (message.getId() != null) {
            throw new ServiceException("Failure \n A new message cannot already have an ID");
        }
        Message result = messageRepository.save(message);
        messageSearchRepository.save(result);
        return result;
    }

    /**
     * Updates an existing message.
     */
    public Message update(Message message){
        log.debug("Service method to update Message : {}", message);
        if (message.getId() == null) {
            return create(message);
        }
        Message result = messageRepository.save(message);
        messageSearchRepository.save(message);
        return result;
    }

    /**
     * Get all the messages.
     */
    public Page<Message> getAll(Integer offset, Integer limit){
        Page<Message> page = messageRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        return page;
    }

    /**
     * Get the "id" message.
     */
    public Message get(Long id) {
        log.debug("Service method to get Message : {}", id);
        return messageRepository.findOne(id);
    }

    /**
     * Delete the "id" message.
     */
    public void delete(Long id) {
        log.debug("Service method to delete Message : {}", id);
        messageRepository.delete(id);
        messageSearchRepository.delete(id);
    }

    /**
     * Search for the message corresponding to the query.
     */
    public List<Message> search(String query) {
        return StreamSupport
            .stream(messageSearchRepository.search(queryString(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
