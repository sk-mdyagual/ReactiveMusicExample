package ec.com.reactive.music.song.routers;

import ec.com.reactive.music.song.dto.SongDTO;
import ec.com.reactive.music.song.usecases.*;
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
public class SongRouter {

    @Bean
    @RouterOperation(path = "/song/all", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = GetSongsUseCase.class, method = RequestMethod.GET, beanMethod = "getAllSongs",
            operation = @Operation(operationId = "getAllSongs", tags = "Song usecases", responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(schema = @Schema(implementation = SongDTO.class))),
                    @ApiResponse(responseCode = "204", description = "Nothing to show")}))
    RouterFunction<ServerResponse> getAllSongsRouter(GetSongsUseCase getSongsUseCase){
        return route(GET("/song/all"),
                request -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(getSongsUseCase.getAllSongs(), SongDTO.class))
                        .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NO_CONTENT).build()));
    }
    @Bean
    @RouterOperation(path = "/song/{songId}", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = GetSongByIdUseCase.class, method = RequestMethod.GET, beanMethod = "getSongById",
            operation = @Operation(operationId = "getSongById", tags = "Song usecases", responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(schema = @Schema(implementation = SongDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Not Found")}))
    RouterFunction<ServerResponse> getSongById(GetSongByIdUseCase getSongByIdUseCase){
        return route(GET("/song/{songId}"),
                request -> getSongByIdUseCase.getSongById(request.pathVariable("songId"))
                        .flatMap(songDTO -> ServerResponse.status(HttpStatus.OK)
                                .bodyValue(songDTO))
                        .onErrorResume(throwable -> ServerResponse.notFound().build()));
    }

    @Bean
    @RouterOperation(path = "/songs/{albumId}", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = GetSongsByAlbumIdUseCase.class, method = RequestMethod.GET, beanMethod = "byAlbumId",
            operation = @Operation(operationId = "getSongByAlbumId", tags = "Song usecases", responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(schema = @Schema(implementation = SongDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Not Found")}))
    RouterFunction<ServerResponse> getSongsByAlbumId(GetSongsByAlbumIdUseCase getSongsByAlbumIdUseCase){
        return route(GET("/songs/{albumId}"),
                request -> ServerResponse.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromPublisher(getSongsByAlbumIdUseCase
                                .byAlbumId(request.pathVariable("albumId")), SongDTO.class ))
                        .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_FOUND).build()));

    }

    @Bean
    @RouterOperation(path = "/song/save", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = SaveSongUseCase.class, method = RequestMethod.POST, beanMethod = "save",
            operation = @Operation(operationId = "saveSongRouter", tags = "Song usecases", responses = {
                    @ApiResponse(responseCode = "201", description = "Success",
                            content = @Content(schema = @Schema(implementation = SongDTO.class))),
                    @ApiResponse(responseCode = "406",description = "Not acceptable, maybe Song not Found to proceed or something else")}))
    RouterFunction<ServerResponse> saveSongRouter(SaveSongUseCase saveSongUseCase){
        return route(POST("/song/save"),
                request -> request.bodyToMono(SongDTO.class)
                        .flatMap(songDTO-> saveSongUseCase.save(songDTO)
                                .flatMap(result -> ServerResponse.status(HttpStatus.CREATED)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(result)))
                        .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).build()));
    }

    @Bean
    @RouterOperation(path = "/song/update/{songId}", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = UpdateSongUseCase.class, method = RequestMethod.PUT, beanMethod = "update",
            operation = @Operation(operationId = "updateSongRouter", tags = "Song usecases", responses = {
                    @ApiResponse(responseCode = "202", description = "Updated",
                            content = @Content(schema = @Schema(implementation = SongDTO.class))),
                    @ApiResponse(responseCode = "304",description = "Not acceptable, maybe Song was not found to proceed updating")}))
    RouterFunction<ServerResponse> updateSongRouter(UpdateSongUseCase updateSongUseCase){
        return route(PUT("/song/update/{songId}").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(SongDTO.class)
                        .flatMap(songDTO -> updateSongUseCase.update(request.pathVariable("songId"),songDTO))
                        .flatMap(result -> ServerResponse.status(HttpStatus.ACCEPTED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(result))
                        .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_MODIFIED).build()));
    }
    @Bean
    @RouterOperation(path = "/song/delete/{songId}", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = DeleteSongByIdUseCase.class, method = RequestMethod.DELETE, beanMethod = "apply",
            operation = @Operation(operationId = "updateSongRouter", tags = "Song usecases", responses = {
                    @ApiResponse(responseCode = "202", description = "Deleted"),
                    @ApiResponse(responseCode = "404",description = "Song does not exist")}))
    RouterFunction<ServerResponse>  deleteSongRouter(DeleteSongByIdUseCase deleSongByIdUseCase){
        return route(DELETE("/song/delete/{songId}").and(accept(MediaType.APPLICATION_JSON)),
                request -> deleSongByIdUseCase.apply(request.pathVariable("songId"))
                        .flatMap(s -> ServerResponse.status(HttpStatus.ACCEPTED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(s))
                        .onErrorResume(throwable ->  ServerResponse.notFound().build()));
    }
}
