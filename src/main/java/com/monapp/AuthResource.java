package com.monapp;

import io.smallrye.jwt.build.Jwt;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.Strings;

import java.time.Duration;
import java.util.Set;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    private static final String USER = "Max";
    private static final String PWD = "secret";

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        if (!Strings.CI.equals(USER, request.username) || !(Strings.CI.equals(PWD, request.password))) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\" : \"Identifiants invalides\"}")
                    .build();
        }

        String token = Jwt.issuer("https://monapp.com")
                .subject(request.username)
                .groups(Set.of("user"))
                .expiresIn(Duration.ofSeconds(360))
                .sign();

        return Response.ok("{\"token\" : \"" + token + "\"}").build();
    }

    public static class LoginRequest {
        public String username;
        public String password;
    }
}
