package menuing.entity;

import java.io.Serializable;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@Entity
@Table(name = "taste_allergy")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQuery(name = TasteAllergy.FIND_ALL, query = "select g from TasteAllergy g")
public class TasteAllergy implements Serializable{
    
    public static final String FIND_ALL = "findAllTastesAllergies";

    @EmbeddedId
    private TasteAllergyKey key;
    
    @NotNull
    private boolean taste;

    @NotNull
    private boolean allergy;

    
    @Override
    public String toString() {
        return new StringBuilder("TasteAllergy [")
                .append(taste).append(", ")
                .append(allergy).append("]").toString();
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("taste", String.valueOf(this.taste))
                .add("allergy", String.valueOf(this.allergy)
                )
                .build();
    }

}
