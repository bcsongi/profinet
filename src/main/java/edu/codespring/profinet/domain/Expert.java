package edu.codespring.profinet.domain;

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
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A Expert.
 */
@Entity
@Table(name = "EXPERT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="expert")
//@PrimaryKeyJoinColumn(name="ID", referencedColumnName="ID")
public class Expert implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull    
    @Column(name = "timetable", nullable = false)
    private String timetable;

    @NotNull    
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "last_active", nullable = false)
    private DateTime lastActive;

    @NotNull    
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @NotNull    
    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @NotNull
    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "regio", nullable = false)
    private String regio;
    
    @Column(name = "locality", nullable = false)
    private String locality;
    
    @Column(name = "route", nullable = false)
    private String route;
    
    @Column(name = "street_number", nullable = false)
    private Long street_number;
    
   /* @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "EXPERT_EXPERTFIELD",
               joinColumns = @JoinColumn(name="experts_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="expertfields_id", referencedColumnName="ID"))
    private Set<Field> expertfields = new HashSet<>();
*/
    @ManyToMany(fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "EXPERT_EXPERTKEYWORD",
               joinColumns = @JoinColumn(name="experts_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="expertkeywords_id", referencedColumnName="ID"))
    private Set<Keyword> expertkeywords = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "EXPERT_EXPERTLANGUAGE",
               joinColumns = @JoinColumn(name="experts_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="expertlanguages_id", referencedColumnName="ID"))
    private Set<Language> expertlanguages = new HashSet<>();

    @OneToOne
    @JoinColumn(name="user_id")
    //@JsonIgnore
    private User expertuser;
    
    @Column(name="active")
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

	/* public Set<Field> getExpertfields() {
        return expertfields;
    }

    public void setExpertfields(Set<Field> fields) {
        this.expertfields = fields;
    }
*/
    public Set<Keyword> getExpertkeywords() {
        return expertkeywords;
    }

    public void setExpertkeywords(Set<Keyword> keywords) {
        this.expertkeywords = keywords;
    }

    public Set<Language> getExpertlanguages() {
        return expertlanguages;
    }

    public void setExpertlanguages(Set<Language> languages) {
        this.expertlanguages = languages;
    }

    public User getExpertuser() {
        return expertuser;
    }

    public void setExpertuser(User user) {
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

        Expert expert = (Expert) o;

        if ( ! Objects.equals(id, expert.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Expert{" +
                "id=" + id +
                ", timetable='" + timetable + "'" +
                ", phoneNumber='" + phoneNumber + "'" +
                ", lastActive='" + lastActive + "'" +
                ", latitude='" + latitude + "'" +
                ", longitude='" + longitude + "'" +
                '}';
    }
}
