package com.monapp.tasks;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/tasks")
@Authenticated
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskResource {

    @Inject
    TaskService taskService;

    /**
     * Lister toutes les tâches
     *
     * @return List<Task>
     */
    @GET
    @RolesAllowed("user")
    public List<Task> listerTasks() {
        return taskService.listerTout();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("user")
    public Response findById(@PathParam("id") Long id) {
        Task task = taskService.trouverParId(id);
        return null == task ?
                Response.status(Response.Status.NOT_FOUND).build() :
                Response.ok(task).build();
    }

    @POST
    @RolesAllowed("user")
    public Response createTask(Task task) {
        Task created = taskService.creer(task);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response modify(@PathParam("id") Long id, Task modified) {
        Task updated = taskService.modifier(id, modified);
        return null == updated ?
                Response.status(Response.Status.NOT_FOUND).build() :
                Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    public Response delete(@PathParam("id") Long id) {
        return taskService.supprimer(id) ?
                Response.noContent().build() :
                Response.status(Response.Status.NOT_FOUND).build();
    }
}
