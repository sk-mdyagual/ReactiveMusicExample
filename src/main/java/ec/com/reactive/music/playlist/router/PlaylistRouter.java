package ec.com.reactive.music.playlist.router;

import ec.com.reactive.music.playlist.dto.PlaylistDTO;
import ec.com.reactive.music.playlist.usecases.*;
import ec.com.reactive.music.playlist.usecases.interfaces.UpdatePlaylist;
import ec.com.reactive.music.song.dto.SongDTO;
import ec.com.reactive.music.song.usecases.GetSongByIdUseCase;
import ec.com.reactive.music.song.usecases.GetSongsUseCase;
import ec.com.reactive.music.song.usecases.UpdateSongUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class PlaylistRouter {
    @Bean
    @RouterOperation(path = "/playlist/all", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = GetPlaylistsUseCase.class, method = RequestMethod.GET, beanMethod = "applyUseCase",
            operation = @Operation(operationId = "getAllPlaylists", tags = "Playlist usecases", responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(schema = @Schema(implementation = PlaylistDTO.class))),
                    @ApiResponse(responseCode = "204", description = "Nothing to show")}))
    RouterFunction<ServerResponse> getAllPlaylistsRouter(GetPlaylistsUseCase getPlaylistsUseCase){
        return route(GET("/playlist/all"),
                request -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(getPlaylistsUseCase.applyUseCase(), PlaylistDTO.class))
                        .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NO_CONTENT).build()));
    }

    @Bean
    @RouterOperation(path = "/playlist/{playlistId}", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = GetPlaylistByIdUseCase.class, method = RequestMethod.GET, beanMethod = "getPlaylist",
            operation = @Operation(operationId = "getPlaylistById", tags = "Playlist usecases", responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(schema = @Schema(implementation = PlaylistDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Not Found")}))
    RouterFunction<ServerResponse> getPlaylistByIdRouter(GetPlaylistByIdUseCase getPlaylistByIdUseCase){
        return route(GET("/playlist/{playlistId}"),
                request -> getPlaylistByIdUseCase.getPlaylist(request.pathVariable("playlisId"))
                        .flatMap(playlistDTO -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(playlistDTO))
                        .onErrorResume(throwable -> ServerResponse.notFound().build()));
    }
    @Bean
    @RouterOperation(path = "/playlist/save", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = SavePlaylistUseCase.class, method = RequestMethod.POST, beanMethod = "applyUseCase",
            operation = @Operation(operationId = "savePlaylist", tags = "Playlist usecases", responses = {
                    @ApiResponse(responseCode = "201", description = "Created",
                            content = @Content(schema = @Schema(implementation = PlaylistDTO.class))),
                    @ApiResponse(responseCode = "406", description = "Not acceptable, maybe Playlist not Found to proceed or something else")}))
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
    @RouterOperation(path = "/playlist/update/{playlistId}", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = UpdatePlaylistUseCase.class, method = RequestMethod.PUT, beanMethod = "applyUseCase",
            operation = @Operation(operationId = "updatePlaylistRouter", tags = "Playlist usecases", responses = {
                    @ApiResponse(responseCode = "202", description = "Updated",
                            content = @Content(schema = @Schema(implementation = PlaylistDTO.class))),
                    @ApiResponse(responseCode = "304",description = "Not acceptable, maybe Song was not found to proceed updating")}))
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
    @RouterOperation(path = "/playlist/add/{playlistId}/{songId}", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = AddSongUseCase.class, method = RequestMethod.PUT, beanMethod = "addToPlaylist",
            operation = @Operation(operationId = "addSongToPlaylist", tags = "Playlist usecases", responses = {
                    @ApiResponse(responseCode = "202", description = "Song added",
                            content = @Content(schema = @Schema(implementation = PlaylistDTO.class))),
                    @ApiResponse(responseCode = "304",description = "Not acceptable, maybe Song was not found to proceed updating")}))
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
    @RouterOperation(path = "/playlist/remove/{playlistId}/{songId}", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = RemoveSongUseCase.class, method = RequestMethod.PUT, beanMethod = "removeFromPlaylist",
            operation = @Operation(operationId = "removeSongFromPlaylist", tags = "Playlist usecases", responses = {
                    @ApiResponse(responseCode = "202", description = "Song removed",
                            content = @Content(schema = @Schema(implementation = PlaylistDTO.class))),
                    @ApiResponse(responseCode = "304",description = "Not acceptable, maybe Song was not found to proceed updating")}))
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
    @Bean
    @RouterOperation(path = "/playlist/delete/{playlistId}", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = UpdatePlaylistUseCase.class, method = RequestMethod.DELETE, beanMethod = "applyUseCase",
            operation = @Operation(operationId = "deletePlaylistRouter", tags = "Playlist usecases", responses = {
                    @ApiResponse(responseCode = "202", description = "Deleted"),
                    @ApiResponse(responseCode = "404",description = "Playlist does not exist")}))
    public RouterFunction<ServerResponse> deletePlaylistRouter(DeletePlaylistUseCase deletePlaylistUseCase){
        return route(DELETE("/playlist/delete/{playlistId}"),
                request -> deletePlaylistUseCase.applyUseCase(request.pathVariable("playlistId"))
                        .flatMap(result -> ServerResponse.accepted()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(result))
                        .onErrorResume(throwable -> ServerResponse.notFound().build()));
    }


}
