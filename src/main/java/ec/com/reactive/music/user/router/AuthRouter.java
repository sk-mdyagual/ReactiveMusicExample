package ec.com.reactive.music.user.router;

import ec.com.reactive.music.user.collection.AuthenticationRequest;
import ec.com.reactive.music.user.usecases.CurrentUserControllerUseCase;
import ec.com.reactive.music.user.usecases.GetUsernameUseCase;
import ec.com.reactive.music.user.usecases.LoginUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AuthRouter {
    @Bean
    RouterFunction<ServerResponse> loginRouter(LoginUseCase loginUseCase){
        return route(POST("/auth/login"),
                request -> loginUseCase.logIn(request.bodyToMono(AuthenticationRequest.class))
                        /*.onErrorResume(throwable -> ServerResponse.status(HttpStatus.FORBIDDEN).build())*/);
    }

    @Bean
    RouterFunction<ServerResponse> getUsernameRouter(GetUsernameUseCase getUsernameUseCase){
        return route(GET("/user/{username}"),
                request -> getUsernameUseCase.getUser(request.pathVariable("username"))
                        .flatMap(user -> ServerResponse.ok().bodyValue(user)));
    }

    @Bean
    RouterFunction<ServerResponse> getCurrentUser(/*CurrentUserControllerUseCase currentUserControllerUseCase*//*ServerRequest request*/){
        /*return route(GET("/current"),
                request -> currentUserControllerUseCase.current(request.bodyToMono(UserDetails.class))
                        .flatMap(response -> ServerResponse.ok().bodyValue(response)));*/
        return route(GET("/current"),
                r -> ReactiveSecurityContextHolder.getContext()
                .switchIfEmpty(Mono.error(new IllegalStateException("ReactiveSecurityContext is empty")))
                .map(SecurityContext::getAuthentication)
                .map(authentication -> Map.of(
                        "name", authentication.getName(),
                        "roles", AuthorityUtils.authorityListToSet(authentication.getAuthorities())))
                //.doOnNext(System.out::println)
                //.doOnError(Throwable::printStackTrace)
                //.doOnSuccess(s -> System.out.println("completed without value: " + s))
                .flatMap(s -> ServerResponse.ok().bodyValue(s)));
    }


}
