package edu.codespring.profinet.repository;

import edu.codespring.profinet.domain.Appointment;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Appointment entity.
 */
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {

    @Query("select appointment from Appointment appointment where appointment.userappointment.login = ?#{principal.username}")
    List<Appointment> findAllForCurrentUser();
    
    @Query("select appointment from Appointment appointment where appointment.expertField.expert.expertuser.login = ?#{principal.username}")
    List<Appointment> findAllForCurrentExpert();

}
