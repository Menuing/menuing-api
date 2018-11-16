package menuing.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Embeddable
public class TasteAllergyPK implements Serializable {
    
    @Column(name = "user_id", nullable=false)
    public Long userId;

    @Column(name = "ingredient_id",nullable=false)
    public Long ingredientId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }
    
}
