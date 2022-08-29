package ec.com.reactive.music.song.repositories;

import ec.com.reactive.music.song.collections.Song;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISongRepository extends ReactiveMongoRepository<Song,String> {
}
