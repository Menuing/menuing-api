package menuing.boundary;

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
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import menuing.entity.Token;

@Stateless
@Path("tokens")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class TokenResources {
    @Inject
    Tokens tokens;
    
    @GET
    @Path("/")
    public JsonArray findAll() {
        JsonArrayBuilder list = Json.createArrayBuilder();
        List<Token> all = this.tokens.findAll();
        all.stream()
                .map(m -> m.toJson())
                .forEach(list::add);
        return list.build();
    }
    
    @POST
    public Response save(@Valid Token token) {
        this.tokens.create(token);
        return Response.ok().build();
    }
    
    @DELETE
    public Response delete(@Valid Token token) {
        this.tokens.remove(token);
        return Response.ok().build();
    }
    
    @POST
    @Path("/firebase/{message}")
    public Response firebase(@PathParam("message") String message){
        this.tokens.callFirebase(message);
        return Response.ok().build();
    }
    
}
