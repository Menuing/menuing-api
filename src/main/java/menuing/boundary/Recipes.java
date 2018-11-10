package menuing.boundary;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import menuing.entity.Recipe;

@Stateless
public class Recipes {
    @PersistenceContext(name = "menuing")
    EntityManager em;

    public List<Recipe> findAll() {
        return this.em.createNamedQuery(Recipe.FIND_ALL).getResultList();
    }
    
    public Recipe findById(Long id){
        return this.em.find(Recipe.class, id);
    }

    public Recipe findByName(String name) {
        return this.em.find(Recipe.class, name);
    }

    public void create(Recipe recipe) {
        this.em.persist(recipe);
    }

    public void remove(Recipe recipe) {
        this.em.remove(recipe);
    }
}
