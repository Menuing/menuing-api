package menuing.entity;

import java.net.URI;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "admin")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQuery(name = Admin.FIND_ALL, query = "select g from Admin g")
public class Admin {
    public static final String FIND_ALL = "findAllAdmins";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlTransient
    private Long id;

    @NotNull
    private String username;

    @NotNull
    private String password;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public String toString() {
        return new StringBuilder("Admin [")
                .append(id).append(", ")
                .append(username).append(", ")
                .append(password).append("]").toString();
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("username", this.username)
                .add("password", this.password)
                .build();
    }
}
