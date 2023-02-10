package me.dio.infrastructure.resources;

import io.smallrye.mutiny.Uni;
import me.dio.api.VotingApi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Path("/voting")
public class VotingResource {
    private final VotingApi api;

    public VotingResource(VotingApi api) {
        this.api = api;
    }

    @GET
    @Path("/elections/{id}/candidates")
    public Uni<List<String>> candidates(@PathParam("id") String id) {
        return api.candidates(id);
    }
}
