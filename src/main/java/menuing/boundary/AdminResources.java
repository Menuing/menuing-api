package menuing.boundary;

import java.net.URI;
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
import menuing.api.ResourceUriBuilder;
import menuing.entity.Admin;

@Stateless
@Path("admins")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class AdminResources {
    
    @Inject
    Admins admins;

    @Inject
    ResourceUriBuilder resourceUriBuilder;

    @Context
    UriInfo uriInfo;

    @GET
    public JsonArray findAll() {
        JsonArrayBuilder list = Json.createArrayBuilder();
        List<Admin> all = this.admins.findAll();
        all.stream()
                .map(m -> m.toJson(
                        resourceUriBuilder.createResourceUri(
                                AdminResources.class,
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
        Admin admin = this.admins.findByUsername(username);
        final URI self = resourceUriBuilder.createResourceUri(
                UserResources.class, "findByUsername", admin.getId(), uriInfo
        );
        return admin.toJson(self);
    }

    @POST
    public Response save(@Valid Admin admin) {
        this.admins.create(admin);
        final URI self = resourceUriBuilder.createResourceUri(
                UserResources.class, "findById", admin.getId(), uriInfo
        );
        return Response.created(self).build();
    }
}
