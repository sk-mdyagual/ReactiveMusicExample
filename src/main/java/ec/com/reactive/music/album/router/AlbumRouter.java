package ec.com.reactive.music.album.router;

import ec.com.reactive.music.album.dto.AlbumDTO;
import ec.com.reactive.music.album.usecases.SaveAlbumUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AlbumRouter {

    @Bean
    public RouterFunction<ServerResponse> saveAlbumRouter(SaveAlbumUseCase saveAlbumUseCase){
        return route(
                POST("/saveAlbum").and(accept(MediaType.APPLICATION_JSON)),

            request -> request.bodyToMono(AlbumDTO.class)
                    .flatMap(saveAlbumUseCase::applySaveAlbum)
                    .flatMap(result -> ServerResponse.status(HttpStatus.CREATED)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(result))
        );
    }
}
