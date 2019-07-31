package edu.codespring.profinet.repository.search;

import java.util.List;

import edu.codespring.profinet.domain.Appointment;

public interface AppointmentSearchRepositoryCustom {
	
	public List<Appointment> findInUsersAppointment(String query);
	
	public List<Appointment> findInExpertsAppointment(String query);
	
}
