package menuing.api;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

/**
 * Responsible to build URI for resources class
 *
 */
public class ResourceUriBuilder {

    public URI createResourceUri(Class<?> resourcesClass, UriInfo uriInfo) {
        return uriInfo.getBaseUriBuilder().path(resourcesClass).build();
    }

    public URI createResourceUri(Class<?> resourcesClass, String method, long id, UriInfo uriInfo) {
        return uriInfo.getBaseUriBuilder().path(resourcesClass).path(resourcesClass, method).build(id);
    }
}
