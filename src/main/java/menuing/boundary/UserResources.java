package menuing.boundary;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import menuing.entity.User;


@Stateless
@Path("users")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class UserResources {

    @Inject
    Users users;

    @Context
    UriInfo uriInfo;

    @GET
    public JsonArray findAll() {
        JsonArrayBuilder list = Json.createArrayBuilder();
        List<User> all = this.users.findAll();
        all.stream()
                .map(m -> m.toJson()
                )
                .forEach(list::add);
        return list.build();
    }

    @GET
    @Path("/")
    public JsonObject findByUsername(@QueryParam("username") String username) {
        User user = this.users.findByUsername(username);
        return user.toJson();
    }

    @POST
    public Response save(@Valid User user) {
        this.users.create(user);
        return Response.ok().build();
    }
}
