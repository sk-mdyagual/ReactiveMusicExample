package ec.com.reactive.music.song.routers;

import ec.com.reactive.music.song.usecases.DeleteSongByIdUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SongRouter {
    @Bean
    RouterFunction<ServerResponse>  deleteSongRouter(DeleteSongByIdUseCase deleSongByIdUseCase){
        return route(DELETE("/deleteSong/{id}").and(accept(MediaType.APPLICATION_JSON)),
                request -> deleSongByIdUseCase.apply(request.pathVariable("id"))
                        .onErrorResume(throwable -> Mono.empty())
                        .flatMap(s -> ServerResponse.status(HttpStatus.ACCEPTED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(s))
                        .switchIfEmpty(ServerResponse.notFound().build()));
    }
}
