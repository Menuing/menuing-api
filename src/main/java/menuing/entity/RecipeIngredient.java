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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;



@Entity
@Table(name = "recipe_ingredient")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQuery(name = RecipeIngredient.FIND_ALL, query = "select g from RecipeIngredient g")
public class RecipeIngredient implements Serializable{
        
    public static final String FIND_ALL = "findAllRecipesIngredients";

    @EmbeddedId
    private RecipeIngredientPK key;

    @MapsId("recipeId")
    @ManyToOne()
    private Recipe recipe;

    @MapsId("ingredientId")
    @ManyToOne(optional=false)
    private Ingredient ingredient;
    
    public RecipeIngredientPK getKey() {
        return key;
    }

    public void setKey(RecipeIngredientPK key) {
        this.key = key;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }
    
    @Override
    public String toString() {
        return new StringBuilder("RecipeIngredient [")
                .append(recipe).append(", ")
                .append(ingredient).append("]").toString();
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("recipe", recipe.toJson())
                .add("ingredient", ingredient.toJson()
                )
                .build();
    }
}
