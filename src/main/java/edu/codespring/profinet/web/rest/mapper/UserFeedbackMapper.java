package edu.codespring.profinet.web.rest.mapper;

import org.mapstruct.Mapper;

import edu.codespring.profinet.domain.Appointment;
import edu.codespring.profinet.domain.UserFeedback;
import edu.codespring.profinet.web.rest.dto.UserFeedbackDTO;

@Mapper(componentModel = "spring", uses = {AppointmentMapper.class})
public interface UserFeedbackMapper {

	UserFeedbackDTO userFeedbackToUserFeedbackDTO(UserFeedback userFeedback);
	
	UserFeedback userFeedbackDTOTOUserFeedback(UserFeedbackDTO userFeedbackDTO);
	
    default Appointment appointmentFromId(Long id) {
        if (id == null) {
            return null;
        }
        Appointment appointment = new Appointment();
        appointment.setId(id);
        return appointment;
    }

}
