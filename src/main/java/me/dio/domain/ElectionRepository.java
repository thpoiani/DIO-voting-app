package me.dio.domain;

import io.smallrye.mutiny.Uni;

public interface ElectionRepository {
    Uni<Election> findById(String id);

    void save(Vote election);
}
