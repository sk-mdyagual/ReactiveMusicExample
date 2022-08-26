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
    Mono<ResponseEntity<AlbumDTO>> updateAlbum (String id, AlbumDTO aDto);

    Mono<ResponseEntity<String>> deleteAlbum (String idAlbum);

    //ModelMapper functions
    Album DTOToEntity (AlbumDTO albumDTO);
    AlbumDTO entityToDTO(Album album);


}
