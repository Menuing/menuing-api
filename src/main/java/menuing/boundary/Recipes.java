package menuing.boundary;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import menuing.entity.Recipe;

@Stateless
public class Recipes {
    @PersistenceContext(name = "menuing")
    EntityManager em;

    public List<Recipe> findAll() {
        Query query = this.em.createQuery("select r from Recipe r");
        return query.setMaxResults(10).getResultList();
    }
    
    public Recipe findById(Long id){
        return this.em.find(Recipe.class, id);
    }

    public List<Recipe> findByName(String name) {
        Query query = this.em.createQuery("select r from Recipe r where r.name = :name");
        query.setParameter("name", name);
        return query.setMaxResults(10).getResultList();
    }
    
    public List<Recipe> findByNameLike(String name) {
        Query query = this.em.createQuery("select r from Recipe r where r.name LIKE :name");
        query.setParameter("name", "%"+name+"%");
        return query.setMaxResults(10).getResultList();
    }

    public void create(Recipe recipe) {
        this.em.persist(recipe);
    }

    public void remove(Long id) {
        Recipe recipe = findById(id);
        this.em.remove(recipe);
    }
    
    public Recipe getRandomByUsername(String username){
        Query tasteQuery = this.em.createQuery(
            "SELECT r FROM Recipe r, TastesAllergies ta, Ingredients i, User u" +
            " WHERE u.username = :name AND u.id = ta.id AND ta.taste = true AND" +
            " AND ta.key.ingredientId = i.id AND r.proportions LIKE %i.name%");
        tasteQuery.setParameter("name", username);
        
        Query allergyQuery = this.em.createQuery(
            "SELECT r FROM Recipe r, TastesAllergies ta, Ingredients i, User u" +
            " WHERE u.username = :name AND u.id = ta.id AND ta.taste = true AND" +
            " AND ta.key.ingredientId = i.id AND r.proportions LIKE %i.name%");
        allergyQuery.setParameter("name", username);
        
        List<Recipe> tastesRecipes = tasteQuery.getResultList();
        List<Recipe> allergyRecipes = allergyQuery.getResultList();
        
        tastesRecipes.removeAll(allergyRecipes);
        
        if(tastesRecipes.size() == 0){
            return null;
        }else{
            //TODO PASSAR RECIPES QUE NO RETORNES A FlASK
            return tastesRecipes.get(0);
        }
    }
}
