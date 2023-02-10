package me.dio.api;

import io.smallrye.mutiny.Uni;
import me.dio.domain.Candidate;
import me.dio.domain.Vote;
import me.dio.domain.VotingService;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class VotingApi {
    VotingService service;

    public VotingApi(VotingService service) {
        this.service = service;
    }

    public Uni<List<String>> candidates(String id) {
        return service.findElection(id)
                      .onItem()
                      .transform(election -> election.candidates().stream()
                                                                  .map(Candidate::id)
                                                                  .toList());
    }

    public void vote(String electionId, String candidateId) {
        service.save(new Vote(electionId, candidateId));
    }
}
