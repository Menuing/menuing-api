package menuing.boundary;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import menuing.entity.Ingredient;


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
