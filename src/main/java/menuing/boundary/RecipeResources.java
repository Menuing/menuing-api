package menuing.boundary;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import menuing.entity.Recipe;

@Stateless
@Path("recipes")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class RecipeResources {
    @Inject
    Recipes recipes;

    @Context
    UriInfo uriInfo;

    @GET
    @Path("all")
    public JsonArray findAll() {
        JsonArrayBuilder list = Json.createArrayBuilder();
        List<Recipe> all = this.recipes.findAll();
        all.stream()
                .map(m -> m.toJson())
                .forEach(list::add);
        return list.build();
    }
    
    @GET
    @Path("/id/{id}")
    public JsonObject findById(@PathParam("id") Long id){
        Recipe recipe = this.recipes.findById(id);
        return recipe.toJson();
    }

    @GET
    @Path("/")
    public JsonArray findByName(@QueryParam("name") String name, @QueryParam("nameLike") String nameLike) {
        JsonArrayBuilder list = Json.createArrayBuilder();
        List<Recipe> result;
        if (name != null){
             result = this.recipes.findByName(name);
        }else{
             result = this.recipes.findByNameLike(nameLike);
        }
        result.stream()
                .map(m -> m.toJson()
                )
                .forEach(list::add);
        return list.build();
    }
    
    @GET
    @Path("/getRandom/")
    public JsonObject getRandomRecipe(@QueryParam("username") String username) throws MalformedURLException, ProtocolException, IOException{
        Recipe recipe = this.recipes.getRandomByUsername(username);
        return recipe.toJson();
    }
    
    @GET
    @Path("/getFastToDo/")
    public JsonObject getFastRecipe(@QueryParam("username") String username){
        Recipe recipe = this.recipes.getFastToDoByUsername(username);
        return recipe.toJson();
    }
    
    @GET
    @Path("/getLowCost/")
    public JsonObject getLowCostRecipe(@QueryParam("username") String username){
        Recipe recipe = this.recipes.getFastToDoByUsername(username);
        return recipe.toJson();
    }
    
    @GET
    @Path("/getFirstDish/")
    public JsonObject getFirstDish(@QueryParam("username") String username){
        Recipe recipe = this.recipes.getFirstDish(username);
        return recipe.toJson();
    }
    
    @GET
    @Path("/getSecondDish/")
    public JsonObject getSecondDish(@QueryParam("username") String username){
        Recipe recipe = this.recipes.getSecondDish(username);
        return recipe.toJson();
    }
    
    @GET
    @Path("/getDinnerDish/")
    public JsonObject getDinnerDish(@QueryParam("username") String username){
        Recipe recipe = this.recipes.getDinnerDish(username);
        return recipe.toJson();
    }
    
    @GET
    @Path("/getBreakfast/")
    public JsonObject getBreakfast(@QueryParam("username") String username){
        Recipe recipe = this.recipes.getBreakfast(username);
        return recipe.toJson();
    }
    
    @GET
    @Path("/getDailyDiet/")
    public JsonArray getDailyDiet(@QueryParam("username") String username){
        JsonArrayBuilder list = Json.createArrayBuilder();
        List<Recipe> result = this.recipes.getDailyDiet(username);
        result.stream()
                .map(m -> m.toJson()
                )
                .forEach(list::add);
        return list.build();
    }
    
    @GET
    @Path("/getWeeklyDiet/")
    public JsonArray getWeeklyDiet(@QueryParam("username") String username){
        JsonArrayBuilder list = Json.createArrayBuilder();
        List<Recipe> result = this.recipes.getWeeklyDiet(username);
        result.stream()
                .map(m -> m.toJson()
                )
                .forEach(list::add);
        return list.build();
    }
    
    @GET
    @Path("/getMonthlyDiet/")
    public JsonArray getMonthlyDiet(@QueryParam("username") String username){
        JsonArrayBuilder list = Json.createArrayBuilder();
        List<Recipe> result = this.recipes.getMonthlyDiet(username);
        result.stream()
                .map(m -> m.toJson()
                )
                .forEach(list::add);
        return list.build();
    }

    @POST
    public Response save(@Valid Recipe recipe, @Valid RecipesIngredients recipeIngredients) {
        Long id = this.recipes.create(recipe);
        return Response.ok(id).build();
    }
    
    @PUT
    public Response update(Recipe recipe, @Valid RecipesIngredients recipeIngredients) {
        this.recipes.modify(recipe);
        return Response.ok().build();
    }

    @DELETE
    @Path("/delete/{id}")
    public Response delete(@PathParam("id") Long id){
        this.recipes.remove(id);
        return Response.ok().build();
    }
}
