package me.dio.infrastructure.redis;

import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.keys.ReactiveKeyCommands;
import io.quarkus.redis.datasource.sortedset.ReactiveSortedSetCommands;
import io.smallrye.mutiny.Uni;
import me.dio.domain.Candidate;
import me.dio.domain.Election;
import me.dio.domain.ElectionRepository;
import me.dio.domain.Vote;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class RedisElectionRepository implements ElectionRepository {
    private static final Logger LOGGER = Logger.getLogger(RedisElectionRepository.class);
    private static final String KEY = "election:";

    private final ReactiveSortedSetCommands<String, String> sortedSetCommands;
    private final ReactiveKeyCommands<String> keyCommands;

    public RedisElectionRepository(ReactiveRedisDataSource dataSource) {
        sortedSetCommands = dataSource.sortedSet(String.class, String.class);
        keyCommands = dataSource.key(String.class);
    }

    public Uni<List<String>> findAll() {
        return keyCommands.keys(KEY + "*");
    }

    @Override
    @CacheResult(cacheName = "memory-cache")
    public Uni<Election> findById(@CacheKey String id) {
        LOGGER.info("Retrieving election " + id + " from redis");
        return sortedSetCommands.zrange(KEY + id, 0, -1)
                                .onItem()
                                .transform(candidates -> new Election(id, candidates.stream().map(Candidate::new).toList()));
    }

    @Override
    public void save(Vote vote) {
        sortedSetCommands.zincrby(KEY + vote.electionId(), 1, vote.candidateId())
                         .subscribe()
                         .with(amount -> LOGGER.info("Voting for " + vote.candidateId()));
    }
}
