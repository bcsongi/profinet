package edu.codespring.profinet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A ExpertContactRequest.
 */
@Entity
@Table(name = "EXPERTCONTACTREQUEST")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName="expertcontactrequest")
public class ExpertContactRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    
    @Column(name = "approved")
    private Boolean approved;

    @ManyToOne
    private User userRequest;

    @ManyToOne
    private Expert expertrequest;

    @Column(name = "description")
    private String description;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public User getUserRequest() {
        return userRequest;
    }

    public void setUserRequest(User user) {
        this.userRequest = user;
    }

    public Expert getExpertrequest() {
        return expertrequest;
    }

    public void setExpertrequest(Expert expert) {
        this.expertrequest = expert;
    }

    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExpertContactRequest expertContactRequest = (ExpertContactRequest) o;

        if ( ! Objects.equals(id, expertContactRequest.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ExpertContactRequest{" +
                "id=" + id +
                ", approved='" + approved + "'" +
                '}';
    }
}
