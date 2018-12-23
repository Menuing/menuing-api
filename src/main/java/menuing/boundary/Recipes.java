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
import java.util.Random;
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
        "SELECT rr.recipe FROM RecommendedRecipe rr, User u "
                + "WHERE u.username = :username AND u.id=rr.key.userId");
        recipeQuery.setParameter("username", username);
        
        
        List<Recipe> tastesRecipes = recipeQuery.setMaxResults(10).getResultList();
        
        Random randomGenerator = new Random();
        
        if(tastesRecipes.size() == 0){
            return null;
        }
        return tastesRecipes.get(randomGenerator.nextInt(tastesRecipes.size()));
        
    }

    public Recipe getFastToDoByUsername(String username) {
        Query recipeQuery = this.em.createQuery(
        "SELECT rr.recipe FROM RecommendedRecipe rr, User u, Recipe r "
                + "WHERE u.username = :username AND u.id=rr.key.userId AND rr.key.recipeId=r.id AND r.fast=true");
        recipeQuery.setParameter("username", username);
        
        
        List<Recipe> tastesRecipes = recipeQuery.setMaxResults(10).getResultList();
        
        Random randomGenerator = new Random();
        
        if(tastesRecipes.size() == 0){
            return null;
        }
        return tastesRecipes.get(randomGenerator.nextInt(tastesRecipes.size()));
    }
    
    public Recipe getLowCostByUsername(String username) {
        Query recipeQuery = this.em.createQuery(
        "SELECT rr.recipe FROM RecommendedRecipe rr, User u, Recipe r "
                + "WHERE u.username = :username AND u.id=rr.key.userId AND rr.key.recipeId=r.id AND r.lowCost=true");
        recipeQuery.setParameter("username", username);
        
        
        List<Recipe> tastesRecipes = recipeQuery.setMaxResults(10).getResultList();
        
        Random randomGenerator = new Random();
        
        if(tastesRecipes.size() == 0){
            return null;
        }
        return tastesRecipes.get(randomGenerator.nextInt(tastesRecipes.size()));
    }
}