package ec.com.reactive.music.playlist.usecases.interfaces;

import ec.com.reactive.music.playlist.dto.PlaylistDTO;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface UpdatePlaylist {
    Mono<PlaylistDTO> applyUseCase(String playlistId, PlaylistDTO playlistDTO);
}
