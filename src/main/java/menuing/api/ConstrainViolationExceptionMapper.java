package menuing.api;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.stream.Collectors;

/**
 * Responsible giving a proper message when have a invalid request rather than just give a http code.
 * Instead just sent HTTP Code 400 Bad Request, it's better to give the root cause.
 */
@Provider
public class ConstrainViolationExceptionMapper implements
        ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException e) {
        final String message = e.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining(", "));
        return Response.status(Response.Status.BAD_REQUEST)
                .header("X-Validation-Error", message).build();
    }
}
