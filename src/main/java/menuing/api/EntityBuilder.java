package menuing.api;

import menuing.boundary.MessageResources;
import menuing.boundary.RootResources;
import menuing.entity.GuestBook;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

import static javax.json.Json.createObjectBuilder;


public class EntityBuilder {

    public JsonObject buildIndex(URI self, URI message) {
        final JsonObjectBuilder builder = createObjectBuilder();

        builder.add("_links", Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                        .add("rel", "self")
                        .add("href", self.toString())
                        .add("method", "GET")
                )
                .add(Json.createObjectBuilder()
                        .add("rel", "message")
                        .add("href", message.toString())
                        .add("method", "GET")
                )
        );

        return builder.build();
    }
}
