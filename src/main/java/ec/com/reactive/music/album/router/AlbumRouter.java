package ec.com.reactive.music.album.router;

import ec.com.reactive.music.album.dto.AlbumDTO;
import ec.com.reactive.music.album.usecases.GetAlbumsUseCase;
import ec.com.reactive.music.album.usecases.SaveAlbumUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
//Why we need to use beans? Because we are doing specific/custom implementation
/*Why we need to use configuration? Because indicates that a class declares one or more @Bean methods and may be processed by the Spring container
to generate bean definitions and service requests for those beans at runtime.
In simple words, The @RestController annotation becames @Configuration for the functional approach implemented*/
@Configuration
public class AlbumRouter {
    @Bean //<-- A bean is a method-level annotation which simply a Java object which is created by Spring framework
    public RouterFunction<ServerResponse> getAlbumsRouter(GetAlbumsUseCase getAlbumsUseCase){
        return route(GET("/allAlbums"), //Define the endpoint to be consumed
                //Keep in mind that you will retrieve a flux of AlbumDTO's
                //This .ok() does not mean that the usecase was already consumed
                request -> ServerResponse.ok()
                        //The mediatype is always json because json is yay
                        .contentType(MediaType.APPLICATION_JSON)
                        //Here we are going to call the usecase in order to set up the whole server response answer
                        //BodyInserts.fromPublisher? Remember that Flux is a publisher so, It makes sense the use of that method to get the Flux returned on the usecase
                        //You have to specified which type you are going to return inside the Flux<?>
                        .body(BodyInserters.fromPublisher(getAlbumsUseCase.get(), AlbumDTO.class))
                        //If there is nothing, it falls here with my answer of NO_CONTENT
                        .switchIfEmpty(ServerResponse.status(HttpStatus.NO_CONTENT).build())

        );
    }
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
