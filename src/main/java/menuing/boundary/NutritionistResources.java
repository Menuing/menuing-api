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
import menuing.entity.Nutritionist;


@Stateless
@Path("nutritionists")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class NutritionistResources {
        
    @Inject
    Nutritionists nutritionists;

    @GET
    @Path("all")
    public JsonArray findAll() {
        JsonArrayBuilder list = Json.createArrayBuilder();
        List<Nutritionist> all = this.nutritionists.findAll();
        all.stream()
                .map(m -> m.toJson()
                )
                .forEach(list::add);
        return list.build();
    }
    
    @GET
    @Path("/id/{id}")
    public JsonObject findById(@PathParam("id") Long id){
        Nutritionist nutritionist = this.nutritionists.findById(id);
        return nutritionist.toJson();
    }

    @GET
    @Path("/")
    public JsonArray findByName(@QueryParam("name") String name,  @QueryParam("nameLike") String nameLike) {
        JsonArrayBuilder list = Json.createArrayBuilder();
        List<Nutritionist> result;
        if (name != null){
             result = this.nutritionists.findByName(name);
        }else{
             result = this.nutritionists.findByNameLike(nameLike);
        }
        result.stream()
                .map(m -> m.toJson()
                )
                .forEach(list::add);
        return list.build();
    }

    @POST
    public Response save(@Valid Nutritionist nutritionist) {
        this.nutritionists.create(nutritionist);
        return Response.ok().build();
    }
    
    @DELETE
    @Path("/delete/{id}")
    public Response delete(@PathParam("id") Long id){
        this.nutritionists.remove(id);
        return Response.ok().build();
    }
}
