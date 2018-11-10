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
@Path("messages")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class MessageResources {

    @Inject
    Message message;

    @Inject
    ResourceUriBuilder resourceUriBuilder;

    @Context
    UriInfo uriInfo;

    @GET
    public JsonArray findAll() {
        JsonArrayBuilder list = Json.createArrayBuilder();
        List<User> all = this.message.findAll();
        all.stream()
                .map(m -> m.toJson(
                        resourceUriBuilder.createResourceUri(
                                MessageResources.class,
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
    @Path("{id}")
    public JsonObject findById(@PathParam("id") Long id) {
        User user = this.message.findById(id);
        final URI self = resourceUriBuilder.createResourceUri(
                MessageResources.class, "findById", user.getId(), uriInfo
        );
        return user.toJson(self);
    }

    @POST
    public Response save(@Valid User user) {
        this.message.create(user);
        final URI self = resourceUriBuilder.createResourceUri(
                MessageResources.class, "findById", user.getId(), uriInfo
        );
        return Response.created(self).build();
    }
}
