package com.monapp.auth;

import io.smallrye.jwt.build.Jwt;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.Strings;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Set;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    AuthService authService;

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        Utilisateur user = authService.findById(request.username);

        if (null == user || Strings.CS.equals(request.password, new String(Base64.getDecoder().decode(request.password), Charset.defaultCharset()))) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"Identifiants invalides\"}")
                    .build();
        }

        String token = Jwt.issuer("https://monapp.com")
                .subject(request.username)
                .groups(user.roles)
                .expiresIn(3600)
                .sign();

        return Response.ok("{\"token\":\"" + token + "\"}").build();
    }

    @POST
    @RolesAllowed("admin")
    public Response createUser(LoginRequest request) {
        Utilisateur newUser = new Utilisateur();
        newUser.username = request.username;
        newUser.password = request.password;
        newUser.roles = request.roles;
        Utilisateur created = authService.create(newUser);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @RolesAllowed("admin")
    public Response getUser(LoginRequest loginRequest) {
        Utilisateur user = authService.findById(loginRequest.username);
        return null == user ?
                Response.status(Response.Status.NOT_FOUND).build() :
                Response.ok(user).build();
    }

    @PUT
    @RolesAllowed("admin")
    public Response modify(LoginRequest loginRequest) {
        Utilisateur modified = authService.modify(loginRequest.username, loginRequest.password, loginRequest.roles);
        return null == modified ?
                Response.status(Response.Status.NOT_FOUND).build() :
                Response.ok(modified).build();
    }

    @DELETE
    @RolesAllowed("admin")
    public Response delete(LoginRequest loginRequest) {
        return authService.delete(loginRequest.username) ?
                Response.noContent().build() :
                Response.status(Response.Status.NOT_FOUND).build();
    }

    private static class LoginRequest {
        public String username;
        public String password;
        public Set<String> roles;
    }
}
