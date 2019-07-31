package edu.codespring.profinet.domain;

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
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import edu.codespring.profinet.domain.enumeration.AppointmentStatus;

/**
 * A Appointment.
 */
@Entity
@Table(name = "APPOINTMENT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="appointment")
public class Appointment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "starting_date")
    private DateTime startingDate;
    
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "ending_date")
    private DateTime endingDate;

    @Column(name = "description")
    private String description;

    @Column(name = "rated")
    private Boolean rated;
    
    public Boolean getRated() {
		return rated;
	}

	public void setRated(Boolean rated) {
		this.rated = rated;
	}

	@NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status;

    @ManyToOne
    private ExpertField expertField;

    @ManyToOne
    private User userappointment;

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

    public ExpertField getExpertField() {
        return expertField;
    }

    public void setExpertField(ExpertField expertField) {
        this.expertField = expertField;
    }

    public User getUserappointment() {
        return userappointment;
    }

    public void setUserappointment(User user) {
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

        Appointment appointment = (Appointment) o;

        if ( ! Objects.equals(id, appointment.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", startingDate='" + startingDate + "'" +
                ", endingDate='" + endingDate + "'" +
                ", description='" + description + "'" +
                ", status='" + status + "'" +
                '}';
    }
}
