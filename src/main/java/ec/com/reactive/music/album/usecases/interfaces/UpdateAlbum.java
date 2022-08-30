package ec.com.reactive.music.album.usecases.interfaces;

import ec.com.reactive.music.album.dto.AlbumDTO;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface UpdateAlbum {
    Mono<AlbumDTO> applyUpdateAlbum(String id, AlbumDTO albumDTO);
}
