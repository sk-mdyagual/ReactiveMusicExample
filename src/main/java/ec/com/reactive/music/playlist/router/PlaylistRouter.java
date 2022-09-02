package ec.com.reactive.music.playlist.router;

import ec.com.reactive.music.playlist.dto.PlaylistDTO;
import ec.com.reactive.music.playlist.usecases.GetPlaylistByIdUseCase;
import ec.com.reactive.music.playlist.usecases.GetPlaylistsUseCase;
import ec.com.reactive.music.playlist.usecases.SavePlaylistUseCase;
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
public class PlaylistRouter {
    @Bean
    RouterFunction<ServerResponse> getAllPlaylistsRouter(GetPlaylistsUseCase getPlaylistsUseCase){
        return route(GET("/playlist/all"),
                request -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(getPlaylistsUseCase.applyUseCase(), PlaylistDTO.class))
                        .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NO_CONTENT).build()));
    }

    @Bean
    RouterFunction<ServerResponse> getPlaylistByIdRouter(GetPlaylistByIdUseCase getPlaylistByIdUseCase){
        return route(GET("/playlist/{playlistId}"),
                request -> getPlaylistByIdUseCase.getPlaylist(request.pathVariable("playlisId"))
                        .flatMap(playlistDTO -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(playlistDTO))
                        .onErrorResume(throwable -> ServerResponse.notFound().build()));
    }
    @Bean
    RouterFunction<ServerResponse> savePlaylistRouter(SavePlaylistUseCase savePlaylistUseCase){
        return route(POST("/playlist/save").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(PlaylistDTO.class)
                        .flatMap(playlistDTO -> savePlaylistUseCase.applyUseCase(playlistDTO)
                                .flatMap(result -> ServerResponse.status(HttpStatus.CREATED)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(result))
                                .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).build())
                        ));
    }
}
