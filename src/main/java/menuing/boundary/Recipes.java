package menuing.boundary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import menuing.entity.Ingredient;
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
    
    public void modify(Recipe recipe) {
        this.em.merge(recipe);
    }

    public void remove(Long id) {
        Recipe recipe = findById(id);
        this.em.remove(recipe);
    }
    
    public Recipe getRandomByUsername(String username) throws MalformedURLException, ProtocolException, IOException{        
        Query recipeQuery = this.em.createQuery(
        "SELECT r FROM Recipe r, TasteAllergy ta, User u, RecipeIngredient ri "
                + "WHERE u.username = :username AND u.id=ta.key.userId AND "
                + "ta.taste=true AND ri.key.recipeId=r.id AND ri.key.ingredientId=ta.key.ingredientId AND "
                + "r.id NOT IN (SELECT r.id " +
                    "FROM Recipe r, TasteAllergy ta, User u, RecipeIngredient ri " +
                    "WHERE u.username=:username AND u.id=ta.key.userId AND ta.allergy=true AND "
                + "ta.key.ingredientId=ri.key.ingredientId AND r.id=ri.key.recipeId)"
                + "ORDER BY r.averagePuntuation DESC");
        recipeQuery.setParameter("username", username);
        
        
        List<Recipe> tastesRecipes = recipeQuery.setMaxResults(10).getResultList();
        
        return decideRecipe(tastesRecipes, username);
        
    }

    private Recipe decideRecipe(List<Recipe> tastesRecipes, String username) throws MalformedURLException, ProtocolException, IOException {
        // Fer query agafant els ingredients duna recepta
        // Fer map de id recepta : llista de ingredients
        Map<Long, Recipe> likeProbs = new HashMap<>();
        for(Recipe recipe:tastesRecipes){
            Query recipeQuery = this.em.createQuery(
            "SELECT i FROM Recipe r, Ingredient i, RecipeIngredient ri "
            + "WHERE r.id=:id AND r.id=ri.key.recipeId AND ri.key.ingredientId=i.id"
            );
            recipeQuery.setParameter("id", recipe.getId());
            List<Ingredient> recipeIngredients = recipeQuery.getResultList();
            
            Query userQuery = this.em.createQuery(
            "SELECT i FROM User u, Ingredient i, TasteAllergy ta "
            + "WHERE u.username=:username AND u.id=ta.key.userId AND ta.key.ingredientId=i.id"
            );
            userQuery.setParameter("username", username);
            List<Ingredient> userIngredients = userQuery.getResultList();
            List<Ingredient> matchIngredients = recipeIngredients;
            
            matchIngredients.retainAll(userIngredients);
            int ingrLiked = matchIngredients.size();
            
            likeProbs.put(calculateProb(recipe, ingrLiked, recipeIngredients), recipe);
        }
        
        return likeProbs.get(Collections.max(likeProbs.keySet())); 
    }

    private long calculateProb(Recipe recipe, int ingrLiked, List<Ingredient> recipeIngredients) throws IOException, MalformedURLException, ProtocolException {
        String urlRequest = "https://menuing-predictor.herokuapp.com/recipe_prob?rating="+ String.valueOf(recipe.getAveragePuntuation())
                +"&numIngUser="+ String.valueOf(ingrLiked);
        
        for(Ingredient ing: recipeIngredients){
            urlRequest = urlRequest + "&listIngRecipe=" + ing.getName();
        }
        
        URL url = new URL(urlRequest);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        
        BufferedReader in = new BufferedReader(
            new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
          content.append(inputLine);
        }
        in.close();
        con.disconnect();
        
        String[] splittedContent = content.toString().split(": ");
        String probWithBraquet = splittedContent[splittedContent.length-1];
        return (long) Double.parseDouble(probWithBraquet.substring(0, probWithBraquet.length()-2));
    }
}