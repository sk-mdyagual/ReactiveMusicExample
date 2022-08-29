package ec.com.reactive.music.playlist.repository;

import ec.com.reactive.music.playlist.collection.Playlist;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPlaylistRepository extends ReactiveMongoRepository<Playlist,String> {
}