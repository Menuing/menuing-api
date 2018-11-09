package menuing.boundary;

import id.swhp.javaee.guestbook.ResourceUriBuilder;
import id.swhp.javaee.guestbook.entity.GuestBook;

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

/**
 * @author Sukma Wardana
 * @since 1.0.0
 */
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
        List<GuestBook> all = this.message.findAll();
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
        GuestBook guestBook = this.message.findById(id);
        final URI self = resourceUriBuilder.createResourceUri(
                MessageResources.class, "findById", guestBook.getId(), uriInfo
        );
        return guestBook.toJson(self);
    }

    @POST
    public Response save(@Valid GuestBook guestBook) {
        this.message.create(guestBook);
        final URI self = resourceUriBuilder.createResourceUri(
                MessageResources.class, "findById", guestBook.getId(), uriInfo
        );
        return Response.created(self).build();
    }
}
