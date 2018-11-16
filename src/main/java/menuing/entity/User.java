package menuing.entity;

import java.math.BigDecimal;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.net.URI;
import java.util.List;

@Entity
@Table(name = "user_menuing")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQuery(name = User.FIND_ALL, query = "select g from User g")
public class User {

    public static final String FIND_ALL = "findAllUsers";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlTransient
    protected Long id;

    @NotNull
    protected String username;

    @NotNull
    protected String password;

    protected boolean isPremium = false;
    
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="user")
    private List<TasteAllergy> tasteAllergy;

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

    public boolean getIsPremium() {
        return isPremium;
    }

    public void setIsPremium(boolean premium) {
        this.isPremium = premium;
    }

    @Override
    public String toString() {
        return new StringBuilder("User [")
                .append(id).append(", ")
                .append(username).append(", ")
                .append(password).append(", ")
                .append(isPremium).append("]").toString();
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("id", this.id)
                .add("username", this.username)
                .add("password", this.password)
                .add("isPremium", String.valueOf(this.isPremium)
                )
                .build();
    }
}
