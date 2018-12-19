package menuing.entity;

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
@Table(name = "recommended_recipe")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQuery(name = RecipeIngredient.FIND_ALL, query = "select g from RecommendedRecipe g")
public class RecommendedRecipe {
    
    public static final String FIND_ALL = "findAllRecommendedRecipes";

    @EmbeddedId
    private RecommendedRecipePK key;

    @MapsId("recipeId")
    @ManyToOne()
    private Recipe recipe;

    @MapsId("userId")
    @ManyToOne(optional=false)
    private User user;
    
    public RecommendedRecipePK getKey() {
        return key;
    }

    public void setKey(RecommendedRecipePK key) {
        this.key = key;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    @Override
    public String toString() {
        return new StringBuilder("TasteAllergy [")
                .append(user).append(", ")
                .append(recipe).append("]").toString();
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("user", user.toJson())
                .add("recipe", recipe.toJson())
                .build();
    }
}
