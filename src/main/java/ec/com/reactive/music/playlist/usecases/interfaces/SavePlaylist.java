package ec.com.reactive.music.playlist.usecases.interfaces;

import ec.com.reactive.music.playlist.dto.PlaylistDTO;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface SavePlaylist {
    Mono<PlaylistDTO> applyUseCase(PlaylistDTO playlistDTO);
}
