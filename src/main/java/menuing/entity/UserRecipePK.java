package menuing.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class UserRecipePK implements Serializable {
    @Column(name = "user_id", nullable=false)
    public Long userId;

    @Column(name = "recipe_id",nullable=false)
    public Long recipeId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }
}
