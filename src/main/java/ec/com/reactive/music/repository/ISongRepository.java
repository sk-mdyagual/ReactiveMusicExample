package ec.com.reactive.music.repository;

import ec.com.reactive.music.domain.entities.Song;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISongRepository extends ReactiveMongoRepository<Song,String> {
}
