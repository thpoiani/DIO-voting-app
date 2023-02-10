package me.dio.infrastructure.lifecycle;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.pubsub.PubSubCommands;
import io.quarkus.runtime.Startup;
import me.dio.domain.ElectionRepository;
import me.dio.infrastructure.redis.RedisElectionRepository;
import org.jboss.logging.Logger;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import java.util.function.Consumer;

@Startup
@ApplicationScoped
public class Subscribe implements Consumer<String> {
    private static final Logger LOGGER = Logger.getLogger(Subscribe.class);
    private final PubSubCommands<String> pubsub;
    private final PubSubCommands.RedisSubscriber subscriber;
    private final ElectionRepository repository;

    public Subscribe(RedisDataSource dataSource,
                     RedisElectionRepository electionRepository) {
        LOGGER.info("Startup: Subscribe");
        pubsub = dataSource.pubsub(String.class);
        subscriber = pubsub.subscribe("elections", this);
        this.repository = electionRepository;
    }

    @Override
    public void accept(String election) {
        LOGGER.info("Election " + election + " received");
        repository.findById(election).subscribe().with(key -> LOGGER.info("Election " + key.id() + " starting"));
    }

    @PreDestroy
    public void terminate() {
        subscriber.unsubscribe();
    }

}
