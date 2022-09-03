package ec.com.reactive.music.user.router;

import ec.com.reactive.music.user.model.AuthenticationRequest;
import ec.com.reactive.music.user.usecases.CurrentUserControllerUseCase;
import ec.com.reactive.music.user.usecases.GetUsernameUseCase;
import ec.com.reactive.music.user.usecases.LoginUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AuthRouter {
    @Bean
    RouterFunction<ServerResponse> loginRouter(LoginUseCase loginUseCase){
        return route(POST("/user/login"),
                request -> loginUseCase.logIn(request.bodyToMono(AuthenticationRequest.class)));
    }

    @Bean
    RouterFunction<ServerResponse> getUsernameRouter(GetUsernameUseCase getUsernameUseCase){
        return route(GET("/user/{username}"),
                request -> getUsernameUseCase.getUser(request.pathVariable("username"))
                        .flatMap(user -> ServerResponse.ok().bodyValue(user)));
    }

    @Bean
    RouterFunction<ServerResponse> getCurrentUser(CurrentUserControllerUseCase currentUserControllerUseCase){
        return route(GET(""),
                request -> currentUserControllerUseCase.current(request.bodyToMono(UserDetails.class))
                        .flatMap(response -> ServerResponse.ok().bodyValue(response)));
    }


}
