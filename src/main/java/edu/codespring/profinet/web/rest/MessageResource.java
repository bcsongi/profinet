package edu.codespring.profinet.web.rest;

import com.codahale.metrics.annotation.Timed;
import edu.codespring.profinet.domain.Message;
import edu.codespring.profinet.service.MessageService;
import edu.codespring.profinet.service.ServiceException;
import edu.codespring.profinet.web.rest.dto.MessageDTO;
import edu.codespring.profinet.web.rest.mapper.MessageMapper;
import edu.codespring.profinet.web.rest.util.HeaderUtil;
import edu.codespring.profinet.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing Message.
 */
@RestController
@RequestMapping("/api")
public class MessageResource {

    private final Logger log = LoggerFactory.getLogger(MessageResource.class);

    @Inject
    private MessageService messageService;

    @Inject
    private MessageMapper messageMapper;
    
    /**
     * POST  /messages -> Create a new message.
     */
    @RequestMapping(value = "/messages",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MessageDTO> create(@RequestBody MessageDTO messageDTO) throws URISyntaxException {
        log.debug("REST request to save Message : {}", messageDTO);
        
        Message message = messageMapper.messageDTOToMessage(messageDTO);
        MessageDTO result;
        try{
        	result = messageMapper.messageToMessageDTO(messageService.create(message));
        } catch (ServiceException ex){
        	return ResponseEntity.badRequest().header("Failure", "A new message cannot already have an ID").body(null);
        }
        return ResponseEntity.created(new URI("/api/messages/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("message", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /messages -> Updates an existing message.
     */
    @RequestMapping(value = "/messages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MessageDTO> update(@RequestBody MessageDTO messageDTO) throws URISyntaxException {
        log.debug("REST request to update Message : {}", messageDTO);
        Message message = messageMapper.messageDTOToMessage(messageDTO);
        MessageDTO result = messageMapper.messageToMessageDTO(messageService.update(message));
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("message", message.getId().toString()))
                .body(result);
    }

    /**
     * GET  /messages -> get all the messages.
     */
    @RequestMapping(value = "/messages",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<MessageDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Message> page = messageService.getAll(offset, limit);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/messages", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
        		.map(messageMapper::messageToMessageDTO)
        		.collect(Collectors.toList()), headers, HttpStatus.OK);
    }

    /**
     * GET  /messages/:id -> get the "id" message.
     */
    @RequestMapping(value = "/messages/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MessageDTO> get(@PathVariable Long id) {
        log.debug("REST request to get Message : {}", id);
        return Optional.ofNullable(messageService.get(id))
        		.map(messageMapper::messageToMessageDTO)
            .map(messageDTO -> new ResponseEntity<>(
                messageDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /messages/:id -> delete the "id" message.
     */
    @RequestMapping(value = "/messages/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Message : {}", id);
        messageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("message", id.toString())).build();
    }

    /**
     * SEARCH  /_search/messages/:query -> search for the message corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/messages/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MessageDTO> search(@PathVariable String query) {
        return StreamSupport
        		.stream(messageService.search(query).spliterator(), false)
        		.map(messageMapper::messageToMessageDTO)
        		.collect(Collectors.toList());
    }
}
