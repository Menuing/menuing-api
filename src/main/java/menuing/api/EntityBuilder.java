package menuing.api;

import menuing.boundary.UserResources;
import menuing.entity.User;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

import static javax.json.Json.createObjectBuilder;


public class EntityBuilder {

    public JsonObject buildIndex(URI self, URI users) {
        final JsonObjectBuilder builder = createObjectBuilder();

        builder.add("_links", Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                        .add("rel", "self")
                        .add("href", self.toString())
                        .add("method", "GET")
                )
                .add(Json.createObjectBuilder()
                        .add("rel", "user")
                        .add("href", users.toString())
                        .add("method", "GET")
                )
        );

        return builder.build();
    }
}
