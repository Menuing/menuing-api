package menuing.boundary;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import menuing.entity.Recipe;
import menuing.entity.RecommendedRecipe;

@Stateless
public class RecommendedRecipes {
    @PersistenceContext(name = "menuing")
    EntityManager em;
    
    @Inject
    Users users;
    
    @Inject
    Recipes recipes;
    
    public List<RecommendedRecipe> findAll() {
        return this.em.createNamedQuery(RecommendedRecipe.FIND_ALL).getResultList();
    }
    
    public List<RecommendedRecipe> findByUserId(Long id){
        Query query = this.em.createQuery("select rr from RecommendedRecipe rr where rr.key.userId = :id");
        query.setParameter("id", id);
        return query.getResultList();
    }
    
    public List<RecommendedRecipe> findByRecipeId(Long id){
        Query query = this.em.createQuery("select rr from RecommendedRecipe rr where rr.key.recipeId = :id");
        query.setParameter("id", id);
        return query.getResultList();
    }
    
    public List<Recipe> findRecipeByUserId(Long id){
        Query query = this.em.createQuery("select rr.recipe from RecommendedRecipe rr where rr.key.userId = :id");
        query.setParameter("id", id);
        List<Recipe> userRecipes = query.getResultList();
        return userRecipes;
    }

    public void create(RecommendedRecipe recommendedRecipe) {
        recommendedRecipe.setUser(users.findById(recommendedRecipe.getKey().getUserId()));
        recommendedRecipe.setRecipe(recipes.findById(recommendedRecipe.getKey().getRecipeId()));
        this.em.persist(recommendedRecipe);
    }

    public void remove(RecommendedRecipe recommendedRecipe) {
        this.em.remove(recommendedRecipe);
    }
}
