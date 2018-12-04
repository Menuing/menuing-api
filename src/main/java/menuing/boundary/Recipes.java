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

    public Long create(Recipe recipe) {
        this.em.persist(recipe);
        this.em.flush();
        return recipe.getId();
    }

    public void remove(Long id) {
        Recipe recipe = findById(id);
        this.em.remove(recipe);
    }
}
