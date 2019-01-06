package menuing.boundary;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import menuing.entity.UserRecipe;
import menuing.entity.UserRecipePK;
import menuing.entity.User;

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
    
    public List<UserRecipe> findByUserIdRecipeId(Long userId, Long recipeId){
        Query query = this.em.createQuery("select ri from UserRecipe ri where ri.key.userId = :userId and ri.key.recipeId = :recipeId");
        query.setParameter("userId", userId);
        query.setParameter("recipeId", recipeId);
        return query.getResultList();
    }

    public void create(UserRecipe userRecipe) {
        userRecipe.setRecipe(recipes.findById(userRecipe.getKey().getRecipeId()));
        userRecipe.setUser(users.findById(userRecipe.getKey().getUserId()));
        this.em.persist(userRecipe);
    }
    
    public void createOrUpdate(UserRecipe userRecipe) {
        List<UserRecipe> userRecipes = findByUserIdRecipeId(userRecipe.getKey().getUserId(), userRecipe.getKey().getRecipeId());
        if(userRecipes.size()!=0){
            this.em.merge(userRecipe);
        }else{
            create(userRecipe);
        }
    }

    public void createOrUpdateByUsername(String username, int id, int punctuation){
        List<User> userList = users.findByUsername(username);
        if(userList.isEmpty())
           return;
        
        User user = userList.get(0);
        
        UserRecipe ur = new UserRecipe();
        UserRecipePK pkur = new UserRecipePK();
        pkur.setRecipeId((long) id);
        pkur.setUserId(user.getId());
        
        ur.setRecipe(recipes.findById((long) id));
        ur.setPuntuation(punctuation);
        ur.setUser(user);
        ur.setKey(pkur);
        
        List<UserRecipe> userRecipes = findByUserIdRecipeId(user.getId(), (long) id);
        if(!userRecipes.isEmpty()){
            this.em.merge(ur);
        }else{
            create(ur);
        }
    }
    
    public void remove(UserRecipe userRecipe) {
        this.em.remove(userRecipe);
    }    
}
