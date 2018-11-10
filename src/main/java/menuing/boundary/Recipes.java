package menuing.boundary;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
        Query query = this.em.createQuery("select r.name from Recipe r where r.name = :name");
        query.setParameter("name", name);
        return (Recipe) query.getSingleResult();
    }

    public void create(Recipe recipe) {
        this.em.persist(recipe);
    }

    public void remove(Recipe recipe) {
        this.em.remove(recipe);
    }
}
