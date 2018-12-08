package menuing.boundary;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import menuing.entity.TasteAllergy;
import menuing.entity.TasteAllergyPK;
import menuing.entity.Ingredient;
import menuing.entity.User;

@Stateless
public class TastesAllergies {
    @PersistenceContext(name = "menuing")
    EntityManager em;
    
    @Inject
    Users users;
    
    @Inject
    Ingredients ingredients;

    public List<TasteAllergy> findAll() {
        return this.em.createNamedQuery(TasteAllergy.FIND_ALL).getResultList();
    }
    
    public TasteAllergy findById(Long id){
        return this.em.find(TasteAllergy.class, id);
    }
    
    public List<TasteAllergy> findByUserId(Long id){
        Query query = this.em.createQuery("select ta from TasteAllergy ta where ta.key.userId = :id");
        query.setParameter("id", id);
        return query.getResultList();
    }
    
    public List<TasteAllergy> findByIngredientId(Long id){
        Query query = this.em.createQuery("select ta from TasteAllergy ta where ta.key.ingredientId = :id");
        query.setParameter("id", id);
        return query.getResultList();
    }

    public void create(TasteAllergy tasteAllergy) {
        tasteAllergy.setUser(users.findById(tasteAllergy.getKey().getUserId()));
        tasteAllergy.setIngredient(ingredients.findById(tasteAllergy.getKey().getIngredientId()));
        this.em.persist(tasteAllergy);
    }

    public void remove(TasteAllergy tasteAllergy) {
        this.em.remove(tasteAllergy);
    }
    
    public void removeTastesAllergiesOfUser(String username, Boolean taste){
        Query query;
        if(taste)
            query = this.em.createQuery("select ta from TasteAllergy ta where ta.user.username = :username and ta.taste = true");
        else
            query = this.em.createQuery("select ta from TasteAllergy ta where ta.user.username = :username and ta.allergy = true");
        
        query.setParameter("username", username);
        List<TasteAllergy> tastesAllergies = query.getResultList();
        if(!tastesAllergies.isEmpty()){
            for(int i = 0; i < tastesAllergies.size(); i++)
                remove(tastesAllergies.get(i));
        } 
    }
    
    
    public List<TasteAllergy> findUserTastesAllergies(String username, Boolean taste){
        Query query;
        if(taste)
            query = this.em.createQuery("select ta from TasteAllergy ta where ta.user.username = :username and ta.taste = true");
        else
            query = this.em.createQuery("select ta from TasteAllergy ta where ta.user.username = :username and ta.allergy = true");
        
        query.setParameter("username", username);
        List<TasteAllergy> tastesAllergies = query.getResultList();
        if(!tastesAllergies.isEmpty())
            return tastesAllergies;
        else
            return null;
    }
    
    public Boolean createByUsernameAndIngredient(String username, List<String> ingredientList, Boolean taste){
        List<User> userList = users.findByUsername(username);
        if(userList.isEmpty()) return false;
        User user = userList.get(0);
        for(int i = 0; i < ingredientList.size(); i++){
            List<Ingredient> in = ingredients.findByName(ingredientList.get(i));
            
            if(!in.isEmpty()){
                TasteAllergyPK pk = new TasteAllergyPK();
                pk.setIngredientId(in.get(0).getId());
                pk.setUserId(user.getId());
                
                TasteAllergy ta = new TasteAllergy();
                ta.setUser(user);
                ta.setIngredient(in.get(0));
                ta.setKey(pk);
                if(taste){
                    ta.setTaste(true);
                    ta.setAllergy(false);
                }else{
                    ta.setTaste(true);
                    ta.setAllergy(false);
                }
                this.em.persist(ta);
            }
        }
        
        return true;
    }
}
