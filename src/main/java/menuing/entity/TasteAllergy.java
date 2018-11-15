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
import javax.persistence.MapsId;
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
    private TasteAllergyPK key;
    
    @MapsId("userId")
    @ManyToOne()
    private User user;
    
    @MapsId("ingredientId")
    @ManyToOne(optional=false)
    private Ingredient ingredient;
    
    @NotNull
    private boolean taste;

    @NotNull
    private boolean allergy;
    

    public TasteAllergyPK getKey() {
        return key;
    }

    public void setKey(TasteAllergyPK key) {
        this.key = key;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public boolean isTaste() {
        return taste;
    }

    public void setTaste(boolean taste) {
        this.taste = taste;
    }

    public boolean isAllergy() {
        return allergy;
    }

    public void setAllergy(boolean allergy) {
        this.allergy = allergy;
    }
    
    @Override
    public String toString() {
        return new StringBuilder("TasteAllergy [")
                .append(user).append(", ")
                .append(ingredient).append(", ")
                .append(taste).append(", ")
                .append(allergy).append("]").toString();
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("user", user.toJson())
                .add("ingredient", ingredient.toJson())
                .add("taste", String.valueOf(this.taste))
                .add("allergy", String.valueOf(this.allergy)
                )
                .build();
    }

}
