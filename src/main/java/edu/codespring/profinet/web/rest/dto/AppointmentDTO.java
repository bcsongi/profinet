package edu.codespring.profinet.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;

import edu.codespring.profinet.domain.ExpertField;
import edu.codespring.profinet.domain.User;
import edu.codespring.profinet.domain.enumeration.AppointmentStatus;

/*
 * A DTO for the Appointment entity
 */
public class AppointmentDTO implements Serializable {
    private Long id;

    private DateTime startingDate;
    
    private DateTime endingDate;

    private String description;

    private Boolean rated;

	@NotNull
    private AppointmentStatus status;

    private ExpertFieldDTO expertField;

    private UserDTO userappointment;

    
    public Boolean getRated() {
		return rated;
	}

	public void setRated(Boolean rated) {
		this.rated = rated;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(DateTime startingDate) {
        this.startingDate = startingDate;
    }

    public DateTime getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(DateTime endingDate) {
        this.endingDate = endingDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public ExpertFieldDTO getExpertField() {
        return expertField;
    }

    public void setExpertField(ExpertFieldDTO expertField) {
        this.expertField = expertField;
    }

    public UserDTO getUserappointment() {
        return userappointment;
    }

    public void setUserappointment(UserDTO user) {
        this.userappointment = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AppointmentDTO appointment = (AppointmentDTO) o;

        if ( ! Objects.equals(id, appointment.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AppointmentDTO{" +
                "id=" + id +
                ", startingDate='" + startingDate + "'" +
                ", endingDate='" + endingDate + "'" +
                ", description='" + description + "'" +
                ", status='" + status + "'" +
                '}';
    }
}
