package com.monapp.auth;

import io.smallrye.jwt.build.Jwt;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.Strings;

import java.util.Set;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    private static final String ADMIN = "admin";
    private static final String A_PWD = "admin123";
    private static final String USER = "user";
    private static final String U_PWD = "secret";

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        Set<String> roles;
        String username;

        if (Strings.CI.equals(ADMIN, request.username) && Strings.CI.equals(A_PWD, request.password)) {
            username = ADMIN;
            roles = Set.of("user", "admin");
        } else if (Strings.CI.equals(USER, request.username) && Strings.CI.equals(U_PWD, request.password)) {
            username = USER;
            roles = Set.of("user");
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"Identifiants invalides\"}")
                    .build();
        }

        String token = Jwt.issuer("https://monapp.com")
                .subject(username)
                .groups(roles)
                .expiresIn(3600)
                .sign();

        return Response.ok("{\"token\":\"" + token + "\"}").build();
    }

    public static class LoginRequest {
        public String username;
        public String password;
    }
}
