package edu.codespring.profinet.web.rest.dto;
import java.io.Serializable;
import java.util.Objects;

/*
 * A DTO for the ExpertField class
 */
public class ExpertFieldDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
    
    private String description;
    
    private Double rating;
    
    private FieldDTO field;

    private ExpertDTO expert;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

    
    public FieldDTO getField() {
        return field;
    }

    public void setField(FieldDTO field) {
        this.field = field;
    }

    public ExpertDTO getExpert() {
        return expert;
    }

    public void setExpert(ExpertDTO expert) {
        this.expert = expert;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

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
                ", description='" + description + "'" +
                '}';
    }
}