package ec.com.reactive.music.repository;

import ec.com.reactive.music.domain.entities.Playlist;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPlaylistRepository extends ReactiveMongoRepository<Playlist,String> {
}