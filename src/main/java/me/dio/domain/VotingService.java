package me.dio.domain;

import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VotingService {
    private final ElectionRepository electionRepository;

    public VotingService(ElectionRepository electionRepository) {
        this.electionRepository = electionRepository;
    }

    public Uni<Election> findElection(String id) {
        return electionRepository.findById(id);
    }
}
