package menuing.boundary;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import menuing.entity.RecipeIngredient;

@Stateless
public class RecipesIngredients {
    
    @PersistenceContext(name = "menuing")
    EntityManager em;
    
    @Inject
    Recipes recipes;
    
    @Inject
    Ingredients ingredients;

    public List<RecipeIngredient> findAll() {
        return this.em.createNamedQuery(RecipeIngredient.FIND_ALL).getResultList();
    }
    
    public RecipeIngredient findById(Long id){
        return this.em.find(RecipeIngredient.class, id);
    }
    
    public List<RecipeIngredient> findByRecipeId(Long id){
        Query query = this.em.createQuery("select ri from RecipeIngredient ri where ri.key.recipeId = :id");
        query.setParameter("id", id);
        return query.getResultList();
    }
    
    public List<RecipeIngredient> findByIngredientId(Long id){
        Query query = this.em.createQuery("select ri from RecipeIngredient ri where ri.key.ingredientId = :id");
        query.setParameter("id", id);
        return query.getResultList();
    }

    public void create(RecipeIngredient recipeIngredient) {
        recipeIngredient.setRecipe(recipes.findById(recipeIngredient.getKey().getRecipeId()));
        recipeIngredient.setIngredient(ingredients.findById(recipeIngredient.getKey().getIngredientId()));
        this.em.persist(recipeIngredient);
    }

    public void remove(RecipeIngredient recipeIngredient) {
        this.em.remove(recipeIngredient);
    }

    public void removeByRecipeId(Long recipeId) {
        Query query = this.em.createQuery("delete from RecipeIngredient ri where ri.key.recipeId = :recipeId"); 
        query.setParameter("recipeId", recipeId);
        query.executeUpdate();
    }
}
