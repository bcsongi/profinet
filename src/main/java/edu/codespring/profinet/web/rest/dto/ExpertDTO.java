package edu.codespring.profinet.web.rest.dto;

import org.joda.time.DateTime;

import edu.codespring.profinet.domain.User;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Expert entity.
 */
public class ExpertDTO implements Serializable {

    private Long id;

    @NotNull
    private String timetable;

    @NotNull
    private String phoneNumber;

    private DateTime lastActive;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;
    
    @NotNull
    private String country;
    
    private String regio;
    
    private String locality;

    private String route;

    private Long street_number;

    private Set<FieldDTO> expertfields = new HashSet<>();
    
    private Set<KeywordDTO> expertkeywords = new HashSet<>();

    private Set<LanguageDTO> expertlanguages = new HashSet<>();

    private User expertuser;
    
    private Boolean active;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTimetable() {
        return timetable;
    }

    public void setTimetable(String timetable) {
        this.timetable = timetable;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public DateTime getLastActive() {
        return lastActive;
    }

    public void setLastActive(DateTime lastActive) {
        this.lastActive = lastActive;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    
    public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRegio() {
		return regio;
	}

	public void setRegio(String regio) {
		this.regio = regio;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public Long getStreet_number() {
		return street_number;
	}

	public void setStreet_number(Long street_number) {
		this.street_number = street_number;
	}

	/*
    public Set<FieldDTO> getExpertfields() {
        return expertfields;
    }

    public void setExpertfields(Set<FieldDTO> fields) {
        this.expertfields = fields;
    }
*/
    public Set<KeywordDTO> getExpertkeywords() {
        return expertkeywords;
    }

    public void setExpertkeywords(Set<KeywordDTO> keywords) {
        this.expertkeywords = keywords;
    }

    public Set<LanguageDTO> getExpertlanguages() {
        return expertlanguages;
    }

    public void setExpertlanguages(Set<LanguageDTO> languages) {
        this.expertlanguages = languages;
    }
    
    public User getExpertuser(){
    	return expertuser;
    }
    
    public void setExpertuser(User user){
    	this.expertuser = user;
    }
    
    public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExpertDTO expertDTO = (ExpertDTO) o;

        if ( ! Objects.equals(id, expertDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

	@Override
	public String toString() {
		return "ExpertDTO [id=" + id + ", timetable=" + timetable + ", phoneNumber=" + phoneNumber + ", lastActive="
				+ lastActive + ", latitude=" + latitude + ", longitude=" + longitude + ", country=" + country
				+ ", regio=" + regio + ", locality=" + locality + ", route=" + route + ", street_number="
				+ street_number + ", expertkeywords=" + expertkeywords + ", expertlanguages=" + expertlanguages
				+ ", expertuser=" + expertuser + ", active=" + active + "]";
	}
   
}
