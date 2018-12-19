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
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import menuing.entity.Ingredient;
import menuing.entity.Recipe;
import menuing.entity.RecommendedRecipe;
import menuing.entity.RecommendedRecipePK;
import menuing.entity.User;

@Stateless
public class RecommendedRecipes {
    @PersistenceContext(name = "menuing")
    EntityManager em;
    
    @Inject
    Users users;
    
    @Inject
    Recipes recipes;
    
    public List<RecommendedRecipe> findAll() {
        return this.em.createNamedQuery(RecommendedRecipe.FIND_ALL).getResultList();
    }
    
    public List<RecommendedRecipe> findByUserId(Long id){
        Query query = this.em.createQuery("select rr from RecommendedRecipe rr where rr.key.userId = :id");
        query.setParameter("id", id);
        return query.getResultList();
    }
    
    public List<RecommendedRecipe> findByRecipeId(Long id){
        Query query = this.em.createQuery("select rr from RecommendedRecipe rr where rr.key.recipeId = :id");
        query.setParameter("id", id);
        return query.getResultList();
    }
    
    public List<Recipe> findRecipeByUserId(Long id){
        Query query = this.em.createQuery("select rr.recipe from RecommendedRecipe rr where rr.key.userId = :id");
        query.setParameter("id", id);
        List<Recipe> userRecipes = query.getResultList();
        return userRecipes;
    }

    public void create(RecommendedRecipe recommendedRecipe) {
        recommendedRecipe.setUser(users.findById(recommendedRecipe.getKey().getUserId()));
        recommendedRecipe.setRecipe(recipes.findById(recommendedRecipe.getKey().getRecipeId()));
        this.em.persist(recommendedRecipe);
    }

    public void remove(RecommendedRecipe recommendedRecipe) {
        this.em.remove(recommendedRecipe);
    }
    
    public void createRecommendedRecipes(String username) throws IOException{
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
        
        List<Recipe> tastesRecipes = recipeQuery.getResultList();
        
        saveLikedRecipes(tastesRecipes, username);
    }
    
    private void saveLikedRecipes(List<Recipe> tastesRecipes, String username) throws IOException {
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
        System.out.println("HERE I AM");
        // RecommendedRecipe objects
        saveUserRecipes(tastesRecipes, username, likeProbs);
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
        con.setConnectTimeout(10000);
        con.setReadTimeout(10000);
        
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

    private void saveUserRecipes(List<Recipe> tastesRecipes, String username, Map<Long, Recipe> likeProbs) {
        Query query = this.em.createQuery("select u from User u where u.username = :username");
        query.setParameter("username", username);
        User user = (User) query.getResultList().get(0);
        for(Recipe recipe : tastesRecipes){
            RecommendedRecipe recommendedRecipe = new RecommendedRecipe();
            RecommendedRecipePK key = new RecommendedRecipePK();
            key.setRecipeId(recipe.getId());
            key.setUserId(user.getId());
            recommendedRecipe.setKey(key);
            recommendedRecipe.setUser(user);
            recommendedRecipe.setRecipe(recipe);
            this.em.persist(recommendedRecipe);
        }
    }
}
