package ec.com.reactive.music.user.repository;

import ec.com.reactive.music.user.collection.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface IUserRepository extends ReactiveMongoRepository<User,String> {
    Mono<User> findByUsername(String username);
}
