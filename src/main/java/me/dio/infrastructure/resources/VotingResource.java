package me.dio.infrastructure.resources;

import io.smallrye.mutiny.Uni;
import me.dio.api.VotingApi;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestResponse;

import javax.transaction.Transactional;
import javax.ws.rs.*;
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

    @POST
    @Path("/elections/{electionId}/candidates/{candidateId}")
    @ResponseStatus(RestResponse.StatusCode.ACCEPTED)
    @Transactional
    public void vote(@PathParam("electionId") String electionId, @PathParam("candidateId") String candidateId) {
        api.vote(electionId, candidateId);
    }
}
