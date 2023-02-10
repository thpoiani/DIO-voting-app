package me.dio.infrastructure.lifecycle;

import io.quarkus.runtime.Startup;
import me.dio.infrastructure.redis.RedisElectionRepository;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;

@Startup
@ApplicationScoped
public class Cache {
    private static final Logger LOGGER = Logger.getLogger(Cache.class);

    public Cache(RedisElectionRepository electionRepository) {
        LOGGER.info("Startup: Cache");
        electionRepository.findAll().await().indefinitely()
                          .forEach(election -> electionRepository.findById(election).await().indefinitely());
    }
}
