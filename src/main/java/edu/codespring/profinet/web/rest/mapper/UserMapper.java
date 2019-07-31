package edu.codespring.profinet.web.rest.mapper;

import org.mapstruct.Mapper;

import edu.codespring.profinet.domain.User;
import edu.codespring.profinet.web.rest.dto.UserDTO;

@Mapper(componentModel = "spring", uses={})
public interface UserMapper {

	UserDTO userToUserDTO(User user);

    User userDTOToUser(UserDTO userDTO);

}
