package ec.com.reactive.music.repository;

import ec.com.reactive.music.domain.entities.Album;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAlbumRepository extends ReactiveMongoRepository<Album,String> {
}
