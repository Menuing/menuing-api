package menuing.boundary;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import menuing.entity.TasteAllergy;


@Stateless
public class TastesAllergies {
    @PersistenceContext(name = "menuing")
    EntityManager em;

    public List<TasteAllergy> findAll() {
        return this.em.createNamedQuery(TasteAllergy.FIND_ALL).getResultList();
    }
    
    public TasteAllergy findById(Long id){
        return this.em.find(TasteAllergy.class, id);
    }

    public void create(TasteAllergy tasteAllergy) {
        this.em.persist(tasteAllergy);
    }

    public void remove(TasteAllergy tasteAllergy) {
        this.em.remove(tasteAllergy);
    }
}
