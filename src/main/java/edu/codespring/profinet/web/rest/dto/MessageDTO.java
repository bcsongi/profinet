package edu.codespring.profinet.web.rest.dto;

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
 * A DTO for the Message entity.
 */
public class MessageDTO implements Serializable {

    private Long id;

    private String text;

    private DateTime date;

    private UserDTO userfrom;

    private UserDTO userto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public UserDTO getUserfrom() {
        return userfrom;
    }

    public void setUserfrom(UserDTO user) {
        this.userfrom = user;
    }

    public UserDTO getUserto() {
        return userto;
    }

    public void setUserto(UserDTO user) {
        this.userto = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MessageDTO message = (MessageDTO) o;

        if ( ! Objects.equals(id, message.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "id=" + id +
                ", text='" + text + "'" +
                ", date='" + date + "'" +
                '}';
    }
}
