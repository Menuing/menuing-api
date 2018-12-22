package menuing.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RecommendedRecipePK implements Serializable{
    
    @Column(name = "recipe_id", nullable=false)
    public Long recipeId;

    @Column(name = "user_id",nullable=false)
    public Long userId;
    
    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }    
}
