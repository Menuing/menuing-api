package menuing.boundary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
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
    
    public void removeByUserId(Long id){
        Query query = em.createQuery("DELETE FROM RecommendedRecipe rr WHERE rr.key.userId = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
    
    public void createRecommendedRecipes(String username) throws IOException{
        Query recipeQuery = this.em.createQuery(
        "SELECT r FROM Recipe r, TasteAllergy ta, User u, RecipeIngredient ri, Ingredient i "
                + "WHERE u.username = :username AND u.id=ta.key.userId AND "
                + "i.id=ta.key.ingredientId AND "
                + "ta.taste=true AND ri.key.recipeId=r.id AND "
                + "ri.key.ingredientId=i.id AND r.averagePuntuation>3.5 AND "
                + "r.id NOT IN (SELECT r.id " +
                    "FROM Recipe r, TasteAllergy ta, User u, RecipeIngredient ri, Ingredient i " +
                    "WHERE u.username=:username AND u.id=ta.key.userId AND ta.allergy=true AND "
                + "i.id=ta.key.ingredientId AND i.id=ri.key.ingredientId AND r.id=ri.key.recipeId) AND "
                + "r.id NOT IN (SELECT r.id " +
                    "FROM Recipe r, RecipeIngredient ri, Ingredient i " +
                    "WHERE i.name='cocktail' AND "
                + "i.id=ri.key.ingredientId AND r.id=ri.key.recipeId)"
                + "ORDER BY r.averagePuntuation DESC");
        recipeQuery.setParameter("username", username);
        
        List<Recipe> tastesRecipes = recipeQuery.setMaxResults(200).getResultList();
        System.out.println("RECOMMENDED entro");
        saveLikedRecipes(tastesRecipes, username);
    }
    
    private void saveLikedRecipes(List<Recipe> tastesRecipes, String username) throws IOException {
        // Fer query agafant els ingredients duna recepta
        // Fer map de id recepta : llista de ingredients
        Map<Long, Float> likeProbs = new HashMap<>();
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
            List<Ingredient> matchIngredients = new ArrayList<>();
            
            for(Ingredient ingredient : recipeIngredients){
                matchIngredients.add(ingredient);
            }
            
            matchIngredients.retainAll(userIngredients);
            int ingrLiked = matchIngredients.size();
            
            try{
                likeProbs.put(recipe.getId(), calculateProb(recipe, ingrLiked, recipeIngredients));
            }catch(IOException exception){
                System.out.println(exception);
                likeProbs.put(recipe.getId(), (float)0);
            }
        }
        // RecommendedRecipe objects
        saveUserRecipes(tastesRecipes, username, likeProbs);
    }

    private float calculateProb(Recipe recipe, int ingrLiked, List<Ingredient> recipeIngredients) throws IOException, MalformedURLException, ProtocolException {
        String urlRequest = "https://menuing-predictor.herokuapp.com/recipe_prob?rating="+ String.valueOf(recipe.getAveragePuntuation())
                +"&numIngUser="+ String.valueOf(ingrLiked);
        
        for(Ingredient ing: recipeIngredients){
            urlRequest = urlRequest + "&listIngRecipe=" + URLEncoder.encode(ing.getName(), "UTF-8");
        }
        
        URLConnection con = new URL(urlRequest).openConnection();
        con.setRequestProperty("Accept-Charset", "UTF-8");
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
        
        String[] splittedContent = content.toString().split(": ");
        String probWithBraquet = splittedContent[splittedContent.length-1];
        return (float) Double.parseDouble(probWithBraquet.substring(0, probWithBraquet.length()-2));
    }

    private void saveUserRecipes(List<Recipe> tastesRecipes, String username, Map<Long, Float> likeProbs) {
        Query query = this.em.createQuery("select u from User u where u.username = :username");
        query.setParameter("username", username);
        User user = (User)query.getResultList().get(0);
        removeByUserId(user.getId());
        for(Recipe recipe : tastesRecipes){
            if(likeProbs.get(recipe.getId()) > 0.5){
                RecommendedRecipe recommendedRecipe = new RecommendedRecipe();
                RecommendedRecipePK key = new RecommendedRecipePK();
                key.setRecipeId(recipe.getId());
                key.setUserId(user.getId());
                recommendedRecipe.setKey(key);
                recommendedRecipe.setUser(user);
                recommendedRecipe.setRecipe(recipe);
                recommendedRecipe.setLikeProb(likeProbs.get(recipe.getId()));
                this.em.merge(recommendedRecipe);
            }
        }
    }
}
