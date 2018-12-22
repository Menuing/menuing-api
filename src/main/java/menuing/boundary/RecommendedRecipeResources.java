package menuing.boundary;

import java.io.IOException;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import menuing.entity.RecommendedRecipe;


@Stateless
@Path("recommendedRecipes")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class RecommendedRecipeResources {
    @Inject
    RecommendedRecipes recommendedRecipes;
    
    @GET
    @Path("all")
    public JsonArray findAll() {
        JsonArrayBuilder list = Json.createArrayBuilder();
        List<RecommendedRecipe> all = this.recommendedRecipes.findAll();
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
        List<RecommendedRecipe> all = this.recommendedRecipes.findByUserId(userId);
        all.stream()
                .map(m -> m.toJson()
                )
                .forEach(list::add);
        return list.build();
    }
    
    @GET
    @Path("/")
    public JsonArray findByRecipeId(@QueryParam("recipeId") Long recipeId) {
        JsonArrayBuilder list = Json.createArrayBuilder();
        List<RecommendedRecipe> all = this.recommendedRecipes.findByRecipeId(recipeId);
        all.stream()
                .map(m -> m.toJson()
                )
                .forEach(list::add);
        return list.build();
    }

    @POST
    public Response save(@Valid RecommendedRecipe recommendedRecipe) {
        this.recommendedRecipes.create(recommendedRecipe);
        return Response.ok().build();
    }
    
    @POST
    @Path("/calculateRecommendedRecipes")
    public Response calculateRecommendedRecipes(@QueryParam("username") String username) throws IOException, InterruptedException{
        this.recommendedRecipes.createRecommendedRecipes(username);
        return Response.ok().build();
    }
}
