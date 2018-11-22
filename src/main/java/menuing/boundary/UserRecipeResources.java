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
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import menuing.entity.UserRecipe;

@Stateless
@Path("usersRecipes")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class UserRecipeResources {
    
    @Inject
    UsersRecipes usersRecipes;

    @GET
    @Path("all")
    public JsonArray findAll() {
        JsonArrayBuilder list = Json.createArrayBuilder();
        List<UserRecipe> all = this.usersRecipes.findAll();
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
        List<UserRecipe> all = this.usersRecipes.findByRecipeId(userId);
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
        List<UserRecipe> all = this.usersRecipes.findByRecipeId(recipeId);
        all.stream()
                .map(m -> m.toJson()
                )
                .forEach(list::add);
        return list.build();
    }

    @POST
    public Response save(@Valid UserRecipe userRecipe) {
        this.usersRecipes.create(userRecipe);
        return Response.ok().build();
    }
    
    @DELETE
    @Path("/delete")
    public Response delete(@Valid UserRecipe userRecipe){
        this.usersRecipes.remove(userRecipe);
        return Response.ok().build();
    }
}
