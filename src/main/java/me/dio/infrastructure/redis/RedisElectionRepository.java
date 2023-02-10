package me.dio.infrastructure.redis;

import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.sortedset.ReactiveSortedSetCommands;
import io.smallrye.mutiny.Uni;
import me.dio.domain.Candidate;
import me.dio.domain.Election;
import me.dio.domain.ElectionRepository;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RedisElectionRepository implements ElectionRepository {
    private static final Logger LOGGER = Logger.getLogger(RedisElectionRepository.class);

    private final ReactiveSortedSetCommands<String, String> commands;

    public RedisElectionRepository(ReactiveRedisDataSource dataSource) {
        commands = dataSource.sortedSet(String.class, String.class);
    }

    @Override
    @CacheResult(cacheName = "memory-cache")
    public Uni<Election> findById(@CacheKey String id) {
        LOGGER.info("Retrieving election " + id + " from redis");
        return commands.zrange("election:" + id, 0, -1)
                       .onItem()
                       .transform(candidates -> new Election(id, candidates.stream().map(Candidate::new).toList()));
    }
}
