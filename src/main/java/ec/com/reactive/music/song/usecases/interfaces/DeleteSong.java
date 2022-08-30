package ec.com.reactive.music.song.usecases.interfaces;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface DeleteSong {
    Mono<String> apply(String id);
}
