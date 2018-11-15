package menuing.boundary;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import menuing.entity.Ingredient;
import menuing.entity.TasteAllergy;


@Stateless
@Path("tastesAllergies")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class TasteAllergyResources {
    @Inject
    TastesAllergies tastesAllergies;

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
    
    @DELETE
    @Path("/delete")
    public Response delete(@Valid TasteAllergy tasteAllergy){
        this.tastesAllergies.remove(tasteAllergy);
        return Response.ok().build();
    }
}
