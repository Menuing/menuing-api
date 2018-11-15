package menuing.boundary;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import menuing.entity.TasteAllergy;


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
}
