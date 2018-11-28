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
    
    public UserRecipe findByUserIdRecipeId(Long userId, Long recipeId){
        Query query = this.em.createQuery("select ri from UserRecipe ri where ri.key.userId = :userId and ri.key.recipeId = :recipeId");
        query.setParameter("userId", userId);
        query.setParameter("recipeId", recipeId);
        return (UserRecipe)query.getResultList().get(0);
    }

    public void create(UserRecipe userRecipe) {
        userRecipe.setRecipe(recipes.findById(userRecipe.getKey().getRecipeId()));
        userRecipe.setUser(users.findById(userRecipe.getKey().getUserId()));
        this.em.persist(userRecipe);
    }
    
    public void createOrUpdate(UserRecipe userRecipe) {
        UserRecipe actualUsereRecipe = findByUserIdRecipeId(userRecipe.getKey().getUserId(), userRecipe.getKey().getRecipeId());
        if(actualUsereRecipe!=null){
            this.em.merge(userRecipe);
        }else{
            create(userRecipe);
        }
    }

    public void remove(UserRecipe userRecipe) {
        this.em.remove(userRecipe);
    }    
}
