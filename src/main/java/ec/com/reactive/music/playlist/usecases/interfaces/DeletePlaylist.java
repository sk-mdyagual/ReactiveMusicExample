package ec.com.reactive.music.playlist.usecases.interfaces;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface DeletePlaylist {
    Mono<String> applyUseCase(String playlistId);
}
