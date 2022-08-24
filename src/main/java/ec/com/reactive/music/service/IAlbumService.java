package ec.com.reactive.music.service;

import ec.com.reactive.music.domain.dto.AlbumDTO;
import ec.com.reactive.music.domain.entities.Album;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAlbumService {
    Mono<ResponseEntity<Flux<AlbumDTO>>> findAllAlbums();
    Mono<ResponseEntity<AlbumDTO>> findAlbumById(String id);
    Mono<ResponseEntity<AlbumDTO>> saveAlbum(AlbumDTO albumDTO);

    //ModelMapper functions
    Album DTOToEntity (AlbumDTO albumDTO);
    AlbumDTO entityToDTO(Album album);

}
