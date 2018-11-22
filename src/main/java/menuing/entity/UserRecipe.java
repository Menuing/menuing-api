package menuing.entity;

import java.io.Serializable;
import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "user_recipe")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQuery(name = UserRecipe.FIND_ALL, query = "select g from UserRecipe g")
public class UserRecipe implements Serializable {
       
    public static final String FIND_ALL = "findAllUsersRecipes";

    @EmbeddedId
    private UserRecipePK key;

    @MapsId("userId")
    @ManyToOne()
    private User user;
    

    @MapsId("recipeId")
    @ManyToOne(optional=false)
    private Recipe recipe;
    
    private float puntuation = 0;

    public UserRecipePK getKey() {
        return key;
    }

    public void setKey(UserRecipePK key) {
        this.key = key;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public float getPuntuation() {
        return puntuation;
    }

    public void setPuntuation(float puntuation) {
        this.puntuation = puntuation;
    }
    
    @Override
    public String toString() {
        return new StringBuilder("UserRecipe [")
                .append(user).append(", ")
                .append(recipe).append(", ")
                .append(puntuation).append("]").toString();
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("user", user.toJson())
                .add("recipe", recipe.toJson())
                .add("puntuation", String.valueOf(puntuation)
                )
                .build();
    }
}
