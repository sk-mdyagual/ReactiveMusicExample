package ec.com.reactive.music.song.routers;

import ec.com.reactive.music.song.dto.SongDTO;
import ec.com.reactive.music.song.usecases.DeleteSongByIdUseCase;
import ec.com.reactive.music.song.usecases.GetSongByIdUseCase;
import ec.com.reactive.music.song.usecases.GetSongsUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class SongRouter {

    @Bean
    RouterFunction<ServerResponse> getSongById(GetSongByIdUseCase getSongByIdUseCase){
        return route(GET("/song/{id}"),
                request -> getSongByIdUseCase.getSongById(request.pathVariable("id"))
                        .flatMap(songDTO -> ServerResponse.status(HttpStatus.FOUND)
                                .bodyValue(songDTO))
                        .onErrorResume(throwable -> ServerResponse.notFound().build()));
    }
    @Bean
    RouterFunction<ServerResponse> getAllSongsRouter(GetSongsUseCase getSongsUseCase){
        return route(GET("/allSongs"),
                request -> ServerResponse.status(HttpStatus.FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(getSongsUseCase.getAllSongs(), SongDTO.class))
                        .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NO_CONTENT).build()));
    }
    @Bean
    RouterFunction<ServerResponse>  deleteSongRouter(DeleteSongByIdUseCase deleSongByIdUseCase){
        return route(DELETE("/deleteSong/{id}").and(accept(MediaType.APPLICATION_JSON)),
                request -> deleSongByIdUseCase.apply(request.pathVariable("id"))
                        .flatMap(s -> ServerResponse.status(HttpStatus.ACCEPTED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(s))
                        .onErrorResume(throwable ->  ServerResponse.notFound().build()));
    }
}
