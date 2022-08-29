package ec.com.reactive.music.album.repository;

import ec.com.reactive.music.album.collection.Album;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAlbumRepository extends ReactiveMongoRepository<Album,String> {
}
