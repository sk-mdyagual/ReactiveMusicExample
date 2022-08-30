package ec.com.reactive.music.album.router;

import com.mongodb.internal.connection.Server;
import ec.com.reactive.music.album.dto.AlbumDTO;
import ec.com.reactive.music.album.usecases.GetAlbumByIdUseCase;
import ec.com.reactive.music.album.usecases.GetAlbumsUseCase;
import ec.com.reactive.music.album.usecases.SaveAlbumUseCase;
import ec.com.reactive.music.album.usecases.UpdateAlbumUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

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
                        .onErrorResume(throwable ->  ServerResponse.status(HttpStatus.NO_CONTENT).build())

        );
    }
    /*UPDATE: Error handling here*/
    @Bean
    public RouterFunction<ServerResponse> saveAlbumRouter(SaveAlbumUseCase saveAlbumUseCase){
        return route(
                POST("/saveAlbum").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(AlbumDTO.class)
                    .flatMap(albumDTO -> saveAlbumUseCase
                            .applySaveAlbum(albumDTO)
                            //Handle the exception that I dropped on the Mono.errors() inside the usecase
                            //Keep in mind that I will return a Mono empty which automatically activate the switchIfEmpty on line 55
                            //.onErrorResume(throwable -> Mono.empty())) //I'm rewritting the error launched*
                        .flatMap(result -> ServerResponse.status(HttpStatus.CREATED)
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(result))
                            //Error always propagates and it will fall here no matter what level there is
                        .onErrorResume(throwable ->  ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).build())
        ));
    }

    @Bean
    public RouterFunction<ServerResponse> getAlbumById(GetAlbumByIdUseCase getAlbumByIdUseCase){
        return route(GET("/album/{id}"),
                request -> getAlbumByIdUseCase.apply(request.pathVariable("id"))
                        //Keep in mind that I will return a Mono empty which automatically activate the switchIfEmpty on line 68
                        //.onErrorResume(throwable -> Mono.empty())
                        //Otherwise, If everything is ok, I will return a properly answer
                        .flatMap(albumDTO -> ServerResponse.status(HttpStatus.FOUND)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(albumDTO))
                        .onErrorResume(throwable ->  ServerResponse.notFound().build()));
    }

    @Bean
    public RouterFunction<ServerResponse> updateAlbum(UpdateAlbumUseCase updateAlbumUseCase){
        return route(PUT("/update/{id}").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(AlbumDTO.class)
                        .flatMap(albumDTO -> updateAlbumUseCase.applyUpdateAlbum(request.pathVariable("id"),albumDTO)
                                //.onErrorResume(throwable -> Mono.empty())) //Handling the error in other to activate switchIfEmpty on line 83
                        .flatMap(result -> ServerResponse.status(HttpStatus.ACCEPTED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(result))
                        .onErrorResume(throwable ->  ServerResponse.status(HttpStatus.NOT_MODIFIED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new AlbumDTO()))));
    }
}
