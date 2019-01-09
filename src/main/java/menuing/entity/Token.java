package menuing.entity;

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

@Entity
@Table(name = "token")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQuery(name = Token.FIND_ALL, query = "select g from Token g")
public class Token {
    
    public static final String FIND_ALL = "findAllTokens";

    @Id
    protected String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    @Override
    public String toString() {
        return new StringBuilder("Token [")
                .append(token).append("]").toString();
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("token", this.token)
                .build();
    }
}
