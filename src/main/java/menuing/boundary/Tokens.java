package menuing.boundary;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import menuing.entity.Token;
import menuing.entity.User;

@Stateless
public class Tokens {
    @PersistenceContext(name = "menuing")
    EntityManager em;
    
    public List<Token> findAll() {
        return this.em.createNamedQuery(Token.FIND_ALL).getResultList();
    }
    
    public void create(Token token) {
        this.em.persist(token);
    }
    
    public void remove(Token token) {
        this.em.remove(token);
    }
    
    public void callFirebase(String message){
        
    }
}
