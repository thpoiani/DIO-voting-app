package me.dio.infrastructure.lifecycle;

import me.dio.infrastructure.redis.RedisElectionRepository;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;

@io.quarkus.runtime.Startup
@ApplicationScoped
public class Startup {
    private static final Logger LOGGER = Logger.getLogger(Startup.class);

    public Startup(RedisElectionRepository electionRepository) {
        LOGGER.info("Running startup");
        electionRepository.findAll().await().indefinitely()
                          .forEach(election -> electionRepository.findById(election).await().indefinitely());
    }
}
