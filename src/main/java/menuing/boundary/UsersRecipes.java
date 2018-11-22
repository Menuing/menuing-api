package menuing.boundary;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import menuing.entity.UserRecipe;


@Stateless
public class UsersRecipes {
    
    @PersistenceContext(name = "menuing")
    EntityManager em;
    
    @Inject
    Recipes recipes;
    
    @Inject
    Users users;

    public List<UserRecipe> findAll() {
        return this.em.createNamedQuery(UserRecipe.FIND_ALL).getResultList();
    }
    
    public UserRecipe findById(Long id){
        return this.em.find(UserRecipe.class, id);
    }
    
    public List<UserRecipe> findByRecipeId(Long id){
        Query query = this.em.createQuery("select ri from UserRecipe ri where ri.key.recipeId = :id");
        query.setParameter("id", id);
        return query.getResultList();
    }
    
    public List<UserRecipe> findByUserId(Long id){
        Query query = this.em.createQuery("select ri from UserRecipe ri where ri.key.userId = :id");
        query.setParameter("id", id);
        return query.getResultList();
    }

    public void create(UserRecipe userRecipe) {
        userRecipe.setRecipe(recipes.findById(userRecipe.getKey().getRecipeId()));
        userRecipe.setUser(users.findById(userRecipe.getKey().getUserId()));
        this.em.persist(userRecipe);
    }

    public void remove(UserRecipe userRecipe) {
        this.em.remove(userRecipe);
    }    
}
