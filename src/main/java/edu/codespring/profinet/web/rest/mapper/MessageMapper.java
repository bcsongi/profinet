package edu.codespring.profinet.web.rest.mapper;

import org.mapstruct.Mapper;

import edu.codespring.profinet.domain.Message;
import edu.codespring.profinet.domain.User;
import edu.codespring.profinet.web.rest.dto.MessageDTO;

@Mapper(componentModel = "spring", uses = {UserMapper.class })
public interface MessageMapper {

	MessageDTO messageToMessageDTO(Message message);

	Message messageDTOToMessage(MessageDTO messageDTO);

    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }

}
