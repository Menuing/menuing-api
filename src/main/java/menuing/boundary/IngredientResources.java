package menuing.boundary;

import java.io.StringReader;
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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import menuing.entity.Ingredient;
import menuing.entity.TasteAllergy;
import menuing.boundary.TastesAllergies;

@Stateless
@Path("ingredients")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class IngredientResources {
    @Inject
    Ingredients ingredients;
    TastesAllergies tastesAllergies;

    @GET
    @Path("all")
    public JsonArray findAll() {
        JsonArrayBuilder list = Json.createArrayBuilder();
        List<Ingredient> all = this.ingredients.findAll();
        all.stream()
                .map(m -> m.toJson()
                )
                .forEach(list::add);
        return list.build();
    }
    
    @GET
    @Path("/id/{id}")
    public JsonObject findById(@PathParam("id") Long id){
        Ingredient ingredient = this.ingredients.findById(id);
        return ingredient.toJson();
    }

    @GET
    @Path("/")
    public JsonArray findByName(@QueryParam("name") String name, @QueryParam("nameLike") String nameLike) {
        JsonArrayBuilder list = Json.createArrayBuilder();
        List<Ingredient> result; 
        if (name != null){
             result = this.ingredients.findByName(name);
        }else{
             result = this.ingredients.findByNameLike(nameLike);
        }
        result.stream()
                .map(m -> m.toJson()
                )
                .forEach(list::add);
        return list.build();
    }

    /***
     * Returns the list of ingredients excluding the ones that are in tastes or allergies, depending of the params
     * @param jsonString
     * jsonString contains a boolean that determines if the excluded ingredients are tastes or allergies
     * @return 
     */
    @POST
    @Path("/excludingIngredientList")
    public JsonArray listAllMinusTastesAllergies(String jsonString) {

        JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
        JsonObject jsonobject = jsonReader.readObject();
        jsonReader.close();
        
        String username = jsonobject.getString("username");
        Boolean taste = jsonobject.getBoolean("taste"); // If taste = false means allergies
        
        
        List<Ingredient> result = this.ingredients.findIngredientsWithoutTastesAllergies(username,taste);
        JsonArrayBuilder list = Json.createArrayBuilder();
        result.stream()
                .map(m -> m.toJson()
                )
                .forEach(list::add);
        return list.build();
    }
    
    @POST
    @Path("/userTasteAllergyIngredients")
    public JsonArray listUserTastesAllergiesIngredients(String jsonString) {

        JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
        JsonObject jsonobject = jsonReader.readObject();
        jsonReader.close();
        
        String username = jsonobject.getString("username");
        Boolean taste = jsonobject.getBoolean("taste"); // If taste = false means allergies
        
        List<Ingredient> result = this.ingredients.findTastesAllergiesIngredients(username,taste);
        JsonArrayBuilder list = Json.createArrayBuilder();
        result.stream()
                .map(m -> m.toJson()
                )
                .forEach(list::add);
        return list.build();
    }
    
    
    @POST
    public Response save(@Valid Ingredient ingredient) {
        this.ingredients.create(ingredient);
        return Response.ok().build();
    }
    
    @DELETE
    @Path("/delete/{id}")
    public Response delete(@PathParam("id") Long id){
        this.ingredients.remove(id);
        return Response.ok().build();
    }
}
