package ec.com.reactive.music.album.router;

import ec.com.reactive.music.album.dto.AlbumDTO;
import ec.com.reactive.music.album.usecases.GetAlbumByIdUseCase;
import ec.com.reactive.music.album.usecases.GetAlbumsUseCase;
import ec.com.reactive.music.album.usecases.SaveAlbumUseCase;
import ec.com.reactive.music.album.usecases.UpdateAlbumUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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

//Why we need to use beans? Because we are doing specific/custom implementation
/*Why we need to use configuration? Because indicates that a class declares one or more @Bean methods and may be processed by the Spring container
to generate bean definitions and service requests for those beans at runtime.
In simple words, The @RestController annotation becames @Configuration for the functional approach implemented*/
@Configuration
public class AlbumRouter {
    @Bean //<-- A bean is a method-level annotation which simply a Java object which is created by Spring framework
    @RouterOperation(path = "/album/all", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = GetAlbumsUseCase.class, method = RequestMethod.GET, beanMethod = "get",
            operation = @Operation(operationId = "getAllAlbums", tags = "Album usecases", responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(schema = @Schema(implementation = AlbumDTO.class))),
                    @ApiResponse(responseCode = "204", description = "Nothing to show")}))
    public RouterFunction<ServerResponse> getAlbumsRouter(GetAlbumsUseCase getAlbumsUseCase){
        return route(GET("/album/all"), //Define the endpoint to be consumed
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
    @Bean
    @RouterOperation(path = "/album/{albumId}", produces = {
            MediaType.APPLICATION_JSON_VALUE},
            beanClass = GetAlbumByIdUseCase.class, method = RequestMethod.GET, beanMethod = "apply",
            operation = @Operation(operationId = "getAlbumById", tags = "Album usecases", responses = {
                    @ApiResponse(responseCode = "200", description = "Album found",
                            content = @Content(schema = @Schema(implementation = AlbumDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Album not found")}))
    public RouterFunction<ServerResponse> getAlbumById(GetAlbumByIdUseCase getAlbumByIdUseCase){
        return route(GET("/album/{albumId}"),
                request -> getAlbumByIdUseCase.apply(request.pathVariable("albumId"))
                        //If everything is ok, I will return a properly answer
                        .flatMap(albumDTO -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(albumDTO))
                        //Error always propagates and it will fall here no matter what level there is
                        .onErrorResume(throwable ->  ServerResponse.notFound().build()));
        /*return route(GET("/album/{id}"),
                request -> ServerResponse
                        .status(HttpStatus.FOUND)
                        .body(getAlbumByIdUseCase.apply(request.pathVariable("id")), AlbumDTO.class)
                        .onErrorResume(throwable -> ServerResponse.status(HttpStatus.NOT_FOUND).build())
        );*/
    }

    /*UPDATE: Error handling here*/
    @Bean
    @RouterOperation(path = "/album/save", produces = {
            MediaType.APPLICATION_JSON_VALUE}, beanClass = SaveAlbumUseCase.class, method = RequestMethod.POST, beanMethod = "applySaveAlbum",
            operation = @Operation(operationId = "saveAlbum", tags = "Album usecases",
                    responses = {@ApiResponse(responseCode = "201", description = "Success", content = @Content(schema = @Schema(implementation = AlbumDTO.class))),
                            @ApiResponse(responseCode = "406", description = "Not acceptable. Try again")},
                    requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = AlbumDTO.class)))))
    public RouterFunction<ServerResponse> saveAlbumRouter(SaveAlbumUseCase saveAlbumUseCase){
        return route(
                POST("/album/save").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(AlbumDTO.class)
                        .flatMap(albumDTO -> saveAlbumUseCase
                                .applySaveAlbum(albumDTO)
                                .flatMap(result -> ServerResponse.status(HttpStatus.CREATED)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(result))
                                //Error always propagates and it will fall here no matter what level there is
                                //Handle the exception that I dropped on the Mono.errors() inside the usecase
                                .onErrorResume(throwable ->  ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).build())
                        ));
    }



    @Bean
    @RouterOperation(path = "/album/update/{albumId}", produces = {
            MediaType.APPLICATION_JSON_VALUE}, beanClass = UpdateAlbumUseCase.class, method = RequestMethod.PUT, beanMethod = "applyUpdateAlbum",
            operation = @Operation(operationId = "updateAlbum", tags = "Album usecases",
                    responses = {@ApiResponse(responseCode = "202", description = "Accepted", content = @Content(schema = @Schema(implementation = AlbumDTO.class))),
                            @ApiResponse(responseCode = "406", description = "Not acceptable. Try again")},
                    requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = AlbumDTO.class)))))
    public RouterFunction<ServerResponse> updateAlbum(UpdateAlbumUseCase updateAlbumUseCase){
        return route(PUT("/album/update/{id}").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(AlbumDTO.class)
                        .flatMap(albumDTO -> updateAlbumUseCase.applyUpdateAlbum(request.pathVariable("albumId"),albumDTO)
                                .flatMap(result -> ServerResponse.status(HttpStatus.ACCEPTED)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(result))
                                .onErrorResume(throwable ->  ServerResponse.status(HttpStatus.NOT_MODIFIED)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .build())));
    }
}
