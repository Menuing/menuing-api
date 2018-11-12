package menuing.boundary;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
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
    public JsonArray findByName(@QueryParam("name") String name) {
        JsonArrayBuilder list = Json.createArrayBuilder();
        List<Recipe> all = this.recipes.findByName(name);
        all.stream()
                .map(m -> m.toJson()
                )
                .forEach(list::add);
        return list.build();
    }

    @POST
    public Response save(@Valid Recipe recipe) {
        this.recipes.create(recipe);
        return Response.ok().build();
    }
}
