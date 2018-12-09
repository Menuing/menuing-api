package menuing.boundary;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import menuing.entity.Ingredient;
import menuing.entity.TasteAllergy;


@Stateless
public class Ingredients {
    @PersistenceContext(name = "menuing")
    EntityManager em;

    public List<Ingredient> findAll() {
        return this.em.createNamedQuery(Ingredient.FIND_ALL).getResultList();
    }
    
    public Ingredient findById(Long id){
        return this.em.find(Ingredient.class, id);
    }

    public List<Ingredient> findByName(String name) {
        Query query = this.em.createQuery("select u from Ingredient u where u.name = :name");
        query.setParameter("name", name);
        return query.getResultList();
    }
    
    public List<Ingredient> findByNameLike(String name) {
        Query query = this.em.createQuery("select u from Ingredient u where u.name LIKE :name");
        query.setParameter("name", "%"+name+"%");
        return query.setMaxResults(10).getResultList();
    }
    
    public List<Ingredient> findIngredientsWithoutTastesAllergies(String username, boolean taste){
        List<Ingredient> result = findAll();
        Query query;
        query = this.em.createQuery("select ta from TasteAllergy ta where ta.user.username = :username and (ta.taste = true or ta.allergy = true)");
        query.setParameter("username", username);
        
        
        List<TasteAllergy> tastesAllergies = query.getResultList();
        System.out.println("list of ingredients " + tastesAllergies);
        if(!tastesAllergies.isEmpty()){
            if(!result.isEmpty()){
                for(int i = 0; i < tastesAllergies.size(); i++){
                    if(result.contains(tastesAllergies.get(i).getIngredient())){
                        result.remove(tastesAllergies.get(i).getIngredient());
                    }
                }
            }else{
                return null;
            } 
        }
        return result;
    }
             
    public List<Ingredient> findTastesAllergiesIngredients(String username, Boolean taste){
        List<Ingredient> result = new ArrayList<>();
        Query query;
        if(taste)
            query = this.em.createQuery("select ta from TasteAllergy ta where ta.user.username = :username and ta.taste = true");
        else
            query = this.em.createQuery("select ta from TasteAllergy ta where ta.user.username = :username and ta.allergy = true");
        query.setParameter("username", username);
        
        List<TasteAllergy> tastesAllergies = query.getResultList();
        for(int i = 0; i<tastesAllergies.size();i++)
            result.add(tastesAllergies.get(i).getIngredient());
        
        return result;
    }       
            

    public void create(Ingredient ingredient) {
        this.em.persist(ingredient);
    }
    
    public void modify(Ingredient ingredient) {
        this.em.merge(ingredient);
    }

    public void remove(Long id) {
        Ingredient ingredient = findById(id);
        this.em.remove(ingredient);
    }
}
