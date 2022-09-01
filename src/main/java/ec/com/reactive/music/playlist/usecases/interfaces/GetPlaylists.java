package ec.com.reactive.music.playlist.usecases.interfaces;

import ec.com.reactive.music.playlist.dto.PlaylistDTO;
import reactor.core.publisher.Flux;

@FunctionalInterface
public interface GetPlaylists {
    Flux<PlaylistDTO> applyUseCase();
}
