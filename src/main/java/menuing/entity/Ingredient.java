package menuing.entity;

import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@Entity
@Table(name = "ingredient")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQuery(name = Ingredient.FIND_ALL, query = "select g from Ingredient g")
public class Ingredient {
    
    public static final String FIND_ALL = "findAllIngredients";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlTransient
    private Long id;
    
    @NotNull
    private String name;
    
    @NotNull
    private String nutrients;
    
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="ingredient")
    private List<TasteAllergy> tasteAllergy;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNutrients() {
        return nutrients;
    }

    public void setNutrients(String nutrients) {
        this.nutrients = nutrients;
    }
    
    @Override
    public String toString() {
        return new StringBuilder("Ingredient [")
                .append(id).append(", ")
                .append(name).append(", ")
                .append(nutrients).append("]").toString();
    }
    
    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("id", this.id)
                .add("name", this.name)
                .add("nutrients", this.nutrients)
                .build();
    }
}
