package edu.codespring.profinet.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the ExpertContactRequest entity.
 */
public class ExpertContactRequestDTO implements Serializable {

    private Long id;

    private Boolean approved;

    private UserDTO userRequest;

    private ExpertDTO expertrequest;

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

    public UserDTO getUserRequest() {
        return userRequest;
    }

    public void setUserRequest(UserDTO user) {
        this.userRequest = user;
    }

    public ExpertDTO getExpertrequest() {
        return expertrequest;
    }

    public void setExpertrequest(ExpertDTO expert) {
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

        ExpertContactRequestDTO expertContactRequest = (ExpertContactRequestDTO) o;

        if ( ! Objects.equals(id, expertContactRequest.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ExpertContactRequestDTO{" +
                "id=" + id +
                ", approved='" + approved + "'" +
                '}';
    }
}
