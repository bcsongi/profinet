package edu.codespring.profinet.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.codespring.profinet.domain.util.CustomDateTimeDeserializer;
import edu.codespring.profinet.domain.util.CustomDateTimeSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the UserFeedback entity.
 */
public class UserFeedbackDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

    private String comment;

    private Integer rating;
    
    private DateTime date;

    private AppointmentDTO appointmentuserfeedback;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public AppointmentDTO getAppointmentuserfeedback() {
        return appointmentuserfeedback;
    }

    public void setAppointmentuserfeedback(AppointmentDTO appointment) {
        this.appointmentuserfeedback = appointment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserFeedbackDTO userFeedback = (UserFeedbackDTO) o;

        if ( ! Objects.equals(id, userFeedback.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UserFeedbackDTO{" +
                "id=" + id +
                ", comment='" + comment + "'" +
                ", rating='" + rating + "'" +
                ", date='" + date + "'" +
                ", appointmentuserfeedback='" + appointmentuserfeedback + "'" +
                '}';
    } 
}
