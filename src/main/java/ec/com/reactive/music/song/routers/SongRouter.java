package ec.com.reactive.music.song.routers;

import ec.com.reactive.music.album.usecases.GetAlbumByIdUseCase;
import ec.com.reactive.music.song.dto.SongDTO;
import ec.com.reactive.music.song.usecases.*;
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
    RouterFunction<ServerResponse> getAllSongsRouter(GetSongsUseCase getSongsUseCase){
        return route(GET("/song/all"),
                request -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(getSongsUseCase.getAllSongs(), SongDTO.class))
                        .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NO_CONTENT).build()));
    }
    @Bean
    RouterFunction<ServerResponse> getSongById(GetSongByIdUseCase getSongByIdUseCase){
        return route(GET("/song/{id}"),
                request -> getSongByIdUseCase.getSongById(request.pathVariable("id"))
                        .flatMap(songDTO -> ServerResponse.status(HttpStatus.FOUND)
                                .bodyValue(songDTO))
                        .onErrorResume(throwable -> ServerResponse.notFound().build()));
    }

    @Bean
    RouterFunction<ServerResponse> getSongsByAlbumId(GetSongsByAlbumIdUseCase getSongsByAlbumIdUseCase){
        return route(GET("/songs/{albumId}"),
                request -> ServerResponse.status(HttpStatus.FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(getSongsByAlbumIdUseCase
                                .byAlbumId(request.pathVariable("albumId")), SongDTO.class ))
                        .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_FOUND).build()));

    }

    @Bean
    RouterFunction<ServerResponse> saveSongRouter(GetAlbumByIdUseCase getAlbumByIdUseCase,SaveSongUseCase saveSongUseCase){
        return route(POST("/song/save"),
                request -> request.bodyToMono(SongDTO.class)
                        .flatMap(songDTO -> getAlbumByIdUseCase.apply(songDTO.getIdAlbum())
                                        .flatMap(albumDTO -> saveSongUseCase.save(songDTO)
                                                .flatMap(result -> ServerResponse.status(HttpStatus.CREATED).bodyValue(result)))
                                .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).build()))
                        .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_FOUND).build()));
    }

    @Bean
    RouterFunction<ServerResponse> updateSongRouter(UpdateSongUseCase updateSongUseCase){
        return route(PUT("/song/update/{id}").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(SongDTO.class)
                        .flatMap(songDTO -> updateSongUseCase.update(request.pathVariable("id"),songDTO))
                        .flatMap(result -> ServerResponse.status(HttpStatus.ACCEPTED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(result))
                        .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_MODIFIED).build()));
    }
    @Bean
    RouterFunction<ServerResponse>  deleteSongRouter(DeleteSongByIdUseCase deleSongByIdUseCase){
        return route(DELETE("/song/delete/{id}").and(accept(MediaType.APPLICATION_JSON)),
                request -> deleSongByIdUseCase.apply(request.pathVariable("id"))
                        .flatMap(s -> ServerResponse.status(HttpStatus.ACCEPTED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(s))
                        .onErrorResume(throwable ->  ServerResponse.notFound().build()));
    }
}
