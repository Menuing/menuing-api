package menuing.boundary;

import menuing.api.ResourceUriBuilder;

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
import java.net.URI;
import java.util.List;
import menuing.entity.User;


@Stateless
@Path("users")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class UserResources {

    @Inject
    Users users;

    @Inject
    ResourceUriBuilder resourceUriBuilder;

    @Context
    UriInfo uriInfo;

    @GET
    public JsonArray findAll() {
        JsonArrayBuilder list = Json.createArrayBuilder();
        List<User> all = this.users.findAll();
        all.stream()
                .map(m -> m.toJson(
                        resourceUriBuilder.createResourceUri(
                                UserResources.class,
                                "findById",
                                m.getId(),
                                uriInfo
                                )
                        )
                )
                .forEach(list::add);
        return list.build();
    }

    @GET
    @Path("/")
    public JsonObject findByUsername(@QueryParam("username") String username) {
        User user = this.users.findByUsername(username);
        final URI self = resourceUriBuilder.createResourceUri(
                UserResources.class, "findByUsername", user.getId(), uriInfo
        );
        return user.toJson(self);
    }

    @POST
    public Response save(@Valid User user) {
        this.users.create(user);
        final URI self = resourceUriBuilder.createResourceUri(
                UserResources.class, "findById", user.getId(), uriInfo
        );
        return Response.created(self).build();
    }
}
