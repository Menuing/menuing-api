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
import menuing.entity.Admin;

@Stateless
@Path("admins")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class AdminResources {
    
    @Inject
    Admins admins;

    @Context
    UriInfo uriInfo;

    @GET
    @Path("all")
    public JsonArray findAll() {
        JsonArrayBuilder list = Json.createArrayBuilder();
        List<Admin> all = this.admins.findAll();
        all.stream()
                .map(m -> m.toJson())
                .forEach(list::add);
        return list.build();
    }

    @GET
    @Path("/")
    public JsonArray findByUsername(@QueryParam("username") String username) {
        JsonArrayBuilder list = Json.createArrayBuilder();
        List<Admin> all = this.admins.findByUsername(username);
        all.stream()
                .map(m -> m.toJson())
                .forEach(list::add);
        return list.build();
    }

    @POST
    public Response save(@Valid Admin admin) {
        this.admins.create(admin);
        return Response.ok().build();
    }
}
