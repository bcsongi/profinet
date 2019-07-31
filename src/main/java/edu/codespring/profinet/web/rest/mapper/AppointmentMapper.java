package edu.codespring.profinet.web.rest.mapper;

import org.mapstruct.Mapper;

import edu.codespring.profinet.domain.Appointment;
import edu.codespring.profinet.domain.ExpertField;
import edu.codespring.profinet.domain.User;
import edu.codespring.profinet.web.rest.dto.AppointmentDTO;

@Mapper(componentModel = "spring", uses = {ExpertFieldMapper.class, UserMapper.class })
public interface AppointmentMapper {

	AppointmentDTO appointmentToAppointmentDTO(Appointment appointment);

    Appointment appointmentDTOToAppointment(AppointmentDTO appointmentDTO);

    default ExpertField expertFieldFromId(Long id) {
        if (id == null) {
            return null;
        }
        ExpertField expertField = new ExpertField();
        expertField.setId(id);
        return expertField;
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
