package menuing.boundary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
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
        
        if(tastesRecipes.isEmpty()){
            return getNormalRecipe(username, "r.fast=true");
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
        
        if(tastesRecipes.isEmpty()){
            return getNormalRecipe(username, "r.lowCost=true");
        }
        return tastesRecipes.get(randomGenerator.nextInt(tastesRecipes.size()));
    }

    public Recipe getFirstDish(String username) {
        String recipeConditions = "r.fat>1 AND r.fat<10 AND "
                + "r.calories>200 AND r.calories<400 AND r.sodium<500";
        Query recipeQuery = this.em.createQuery(
        "SELECT rr.recipe FROM RecommendedRecipe rr, User u, Recipe r "
                + "WHERE u.username = :username AND u.id=rr.key.userId AND "
                + "rr.key.recipeId=r.id AND "+recipeConditions);
        recipeQuery.setParameter("username", username);
        
        List<Recipe> tastesRecipes = recipeQuery.setMaxResults(10).getResultList();
        
        Random randomGenerator = new Random();
        
        if(tastesRecipes.isEmpty()){
            return getNormalRecipe(username, recipeConditions);
        }
        return tastesRecipes.get(randomGenerator.nextInt(tastesRecipes.size()));
    }

    public Recipe getSecondDish(String username) {
        String recipeConditions = "r.protein>40 AND " +
                "r.calories>200";
        Query recipeQuery = this.em.createQuery(
        "SELECT rr.recipe FROM RecommendedRecipe rr, User u, Recipe r " +
                "WHERE u.username = :username AND u.id=rr.key.userId AND " +
                "rr.key.recipeId=r.id AND "+recipeConditions);
        recipeQuery.setParameter("username", username);
        
        List<Recipe> tastesRecipes = recipeQuery.setMaxResults(10).getResultList();
        
        Random randomGenerator = new Random();
        
        if(tastesRecipes.isEmpty()){
            return getNormalRecipe(username, recipeConditions);
        }
        return tastesRecipes.get(randomGenerator.nextInt(tastesRecipes.size()));
    }
    
    public Recipe getDinnerDish(String username){
        String recipeConditions = "r.fat<20 AND r.protein>0 AND " +
                "r.calories>200 AND r.calories<500";
        Query recipeQuery = this.em.createQuery(
        "SELECT rr.recipe FROM RecommendedRecipe rr, User u, Recipe r " +
                "WHERE u.username = :username AND u.id=rr.key.userId AND " +
                "rr.key.recipeId=r.id AND "+recipeConditions);
        recipeQuery.setParameter("username", username);
        
        List<Recipe> tastesRecipes = recipeQuery.setMaxResults(10).getResultList();
        
        Random randomGenerator = new Random();
        
        if(tastesRecipes.isEmpty()){
            return getNormalRecipe(username, recipeConditions);
        }
        return tastesRecipes.get(randomGenerator.nextInt(tastesRecipes.size()));
    }
    
    public Recipe getBreakfast(String username){
        String recipeConditions = "r.fat<20 AND r.protein>0 AND " +
                "r.calories>100 AND r.calories<300 AND (r.fat>5 OR r.sodium>100)";
        Query recipeQuery = this.em.createQuery(
        "SELECT rr.recipe FROM RecommendedRecipe rr, User u, Recipe r " +
                "WHERE u.username = :username AND u.id=rr.key.userId AND " +
                "rr.key.recipeId=r.id AND "+recipeConditions);
        recipeQuery.setParameter("username", username);
        
        List<Recipe> tastesRecipes = recipeQuery.setMaxResults(10).getResultList();
        
        Random randomGenerator = new Random();
        
        if(tastesRecipes.isEmpty()){
            return getNormalRecipe(username, recipeConditions);
        }
        return tastesRecipes.get(randomGenerator.nextInt(tastesRecipes.size()));
    }
    
    public Recipe getNormalRecipe(String username, String recipeConditions){
        Query recipeQuery = this.em.createQuery("SELECT r FROM User u, Recipe r " +
                "WHERE u.username = :username AND r.averagePuntuation>4 AND "+recipeConditions);
        recipeQuery.setParameter("username", username);
        
        List<Recipe> tastesRecipes = recipeQuery.setMaxResults(20).getResultList();
        
        Random randomGenerator = new Random();
        return tastesRecipes.get(randomGenerator.nextInt(tastesRecipes.size()));
    }

    public List<Recipe> getDailyDiet(String username) {
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(getBreakfast(username));
        recipes.add(getFirstDish(username));
        recipes.add(getSecondDish(username));
        recipes.add(getDinnerDish(username));
        return recipes;
    }
    
    public List<Recipe> getWeeklyDiet(String username) {
        List<Recipe> recipes = new ArrayList<>();
        for(int i = 0; i>7; i++){
            recipes.addAll(getDailyDiet(username));
        }
        return recipes;
    }
    
    public List<Recipe> getMonthlyDiet(String username){
        List<Recipe> recipes = new ArrayList<>();
        for(int i = 0; i>4; i++){
            recipes.addAll(getWeeklyDiet(username));
        }
        return recipes;
    }
}