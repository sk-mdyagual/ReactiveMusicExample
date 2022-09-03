package ec.com.reactive.music.playlist.router;

import ec.com.reactive.music.playlist.dto.PlaylistDTO;
import ec.com.reactive.music.playlist.usecases.*;
import ec.com.reactive.music.song.usecases.GetSongByIdUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;
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
                                .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).build())));
    }

    @Bean
    public RouterFunction<ServerResponse> updatePlaylistRouter(UpdatePlaylistUseCase updatePlaylistUseCase){
        return route(PUT("/playlist/update/{playlistId}").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(PlaylistDTO.class)
                        .flatMap(playlistDTO -> updatePlaylistUseCase.applyUseCase(request.pathVariable("playlistId"),playlistDTO)
                                .flatMap(result -> ServerResponse.accepted()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(result))
                                .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_MODIFIED)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .build())));
    }

    @Bean
    public RouterFunction<ServerResponse> deletePlaylistRouter(DeletePlaylistUseCase deletePlaylistUseCase){
        return route(DELETE("/playlist/delete/{playlistId}"),
                request -> deletePlaylistUseCase.applyUseCase(request.pathVariable("playlistId"))
                        .flatMap(result -> ServerResponse.accepted()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(result))
                        .onErrorResume(throwable -> ServerResponse.notFound().build()));
    }

    @Bean
    public RouterFunction<ServerResponse> addSongToPlaylist(AddSongUseCase addSongUseCase, GetSongByIdUseCase getSongByIdUseCase){
        return route(PUT("/playlist/add/{playlistId}/{songId}"),
                request -> getSongByIdUseCase.getSongById(request.pathVariable("songId"))
                        .flatMap(songDTO -> addSongUseCase.addToPlaylist(request.pathVariable("playlistId"),songDTO )
                                .flatMap(playlistDTO -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(playlistDTO))
                                .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_MODIFIED).build()))
                        .onErrorResume(throwable -> ServerResponse.notFound().build()));
    }

    @Bean
    public RouterFunction<ServerResponse> removeSongFromPlaylist(RemoveSongUseCase removeSongUseCase, GetSongByIdUseCase getSongByIdUseCase){
        return route(PUT("/playlist/remove/{playlistId}/{songId}"),
                request -> getSongByIdUseCase.getSongById(request.pathVariable("songId"))
                        .flatMap(songDTO -> removeSongUseCase.removeFromPlaylist(request.pathVariable("playlistId"),songDTO )
                                .flatMap(playlistDTO -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(playlistDTO))
                                .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_MODIFIED).build()))
                        .onErrorResume(throwable -> ServerResponse.notFound().build()));
    }
}
