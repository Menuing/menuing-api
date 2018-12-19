package menuing.boundary;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.io.StringReader;
import java.net.ProtocolException;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import menuing.entity.TasteAllergy;


@Stateless
@Path("tastesAllergies")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class TasteAllergyResources {
    @Inject
    TastesAllergies tastesAllergies;
    
    @Inject
    RecommendedRecipes recommendedRecipes;
    
    @GET
    @Path("all")
    public JsonArray findAll() {
        JsonArrayBuilder list = Json.createArrayBuilder();
        List<TasteAllergy> all = this.tastesAllergies.findAll();
        all.stream()
                .map(m -> m.toJson()
                )
                .forEach(list::add);
        return list.build();
    }

    @GET
    @Path("/")
    public JsonArray findByUserId(@QueryParam("userId") Long userId) {
        JsonArrayBuilder list = Json.createArrayBuilder();
        List<TasteAllergy> all = this.tastesAllergies.findByUserId(userId);
        all.stream()
                .map(m -> m.toJson()
                )
                .forEach(list::add);
        return list.build();
    }
    
    @GET
    @Path("/")
    public JsonArray findByIngredientId(@QueryParam("ingredientId") Long ingredientId) {
        JsonArrayBuilder list = Json.createArrayBuilder();
        List<TasteAllergy> all = this.tastesAllergies.findByIngredientId(ingredientId);
        all.stream()
                .map(m -> m.toJson()
                )
                .forEach(list::add);
        return list.build();
    }

    @POST
    public Response save(@Valid TasteAllergy tasteAllergy) {
        this.tastesAllergies.create(tasteAllergy);
        return Response.ok().build();
    }
    
    
    /***
     * 
     * @param jsonString contains the keys username and taste
     * key username is a string equivalent to the user's username
     * key taste is a boolean that tells if you have to look for taste or allergy
     * @return 
     */
    @GET
    @Path("/getUserTastesAllergies")
    public JsonArray findUserTastesAllergies(String jsonString){
        JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
        JsonObject jsonobject = jsonReader.readObject();
        jsonReader.close();
        
        String username = jsonobject.getString("username");
        Boolean taste = jsonobject.getBoolean("taste"); // If taste = false means allergies

        List<TasteAllergy> all = this.tastesAllergies.findUserTastesAllergies(username, taste);
        
        if(all != null){
            if(all.size() > 0){
                JsonArrayBuilder list = Json.createArrayBuilder();
                all.stream()
                    .map(m -> m.toJson()
                    )
                    .forEach(list::add);
                return list.build();
            }else
                return null;
        }else{
            return null;
        }
    }
    
    
    /***
     * Deletes the allergies/tastes saved and creates the new ones
     * key username Mail of the username saving tastes or allergies 
     * key ingredients List of the names of the ingredients chosen
     * key taste Diferences if you have to take tastes or allergies
     * @return 
     */
    @PUT
    @Path("/overrideIngredients")
    public Response saveByUsernameAndIngredients(String jsonString) throws IOException, InterruptedException{
        JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
        JsonObject jsonobject = jsonReader.readObject();
        jsonReader.close();
        
        String username = jsonobject.getString("username");
        
        String ingredientsJsonString = jsonobject.getString("ingredients");
        ArrayList<String> ingredients;
        if(!ingredientsJsonString.isEmpty()){
            if(!(ingredientsJsonString.substring(1, ingredientsJsonString.length()-1).isEmpty())){
                String listingredients = ingredientsJsonString.substring(1, ingredientsJsonString.length()-1).trim();
                ingredients = new ArrayList<>();
                Collections.addAll(ingredients, listingredients.split(","));
            }else
                ingredients = new ArrayList<>();         
        }else{
             ingredients = new ArrayList<>();
        }
        
        Boolean taste = jsonobject.getBoolean("taste");
        
        if(ingredients.isEmpty())
            return Response.serverError().build();
        
        this.tastesAllergies.removeTastesAllergiesOfUser(username, taste);
        Boolean result = this.tastesAllergies.createByUsernameAndIngredient(username, ingredients, taste);
        
        return Response.ok().build();
    }
    
    @DELETE
    @Path("/delete")
    public Response delete(@Valid TasteAllergy tasteAllergy){
        this.tastesAllergies.remove(tasteAllergy);
        return Response.ok().build();
    }
}
