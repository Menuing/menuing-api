package menuing.boundary;

import java.io.IOException;
import java.util.List;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import menuing.entity.User;

@Singleton
public class BackgroundJobManager {

    @PersistenceContext(name="menuing")
    EntityManager em;
    
    @Inject
    RecommendedRecipes recommendedRecipes;
    
    @Schedule(hour="0", minute="0", second="0", persistent=false)
    public void dailyJob() throws IOException {
        List<User> users = this.em.createNamedQuery(User.FIND_ALL).getResultList();
        for(User user : users){
            recommendedRecipes.createRecommendedRecipes(user.getUsername());
        }
    }
} 