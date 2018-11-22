package menuing.entity;

import java.io.Serializable;
import javax.persistence.Column;


public class RecipeIngredientPK implements Serializable {
    
    @Column(name = "recipe_id", nullable=false)
    public Long recipeId;

    @Column(name = "ingredient_id",nullable=false)
    public Long ingredientId;

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }
    
}
