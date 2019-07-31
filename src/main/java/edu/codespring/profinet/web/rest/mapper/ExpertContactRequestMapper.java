package edu.codespring.profinet.web.rest.mapper;

import org.mapstruct.Mapper;

import edu.codespring.profinet.domain.Expert;
import edu.codespring.profinet.domain.ExpertContactRequest;
import edu.codespring.profinet.domain.User;
import edu.codespring.profinet.web.rest.dto.ExpertContactRequestDTO;

@Mapper(componentModel = "spring", uses = {ExpertMapper.class, UserMapper.class })
public interface ExpertContactRequestMapper {

	ExpertContactRequestDTO expertContactRequestToExpertContactRequestDTO(ExpertContactRequest expertContactRequest);
	
	ExpertContactRequest expertContactRequestDTOToExpertContactRequest(ExpertContactRequestDTO expertContactRequestDTO);

    default Expert expertFromId(Long id) {
        if (id == null) {
            return null;
        }
        Expert expert = new Expert();
        expert.setId(id);
        return expert;
    }
    
    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }

}
