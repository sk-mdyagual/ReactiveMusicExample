package ec.com.reactive.music.user.usecases;

import ec.com.reactive.music.user.jwt.JwtTokenProvider;
import ec.com.reactive.music.user.collection.AuthenticationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginUseCase {
    private final JwtTokenProvider jwtTokenProvider;

    private final ReactiveAuthenticationManager authenticationManager;

    public Mono<ServerResponse> logIn(Mono<AuthenticationRequest> authenticationRequest){
        return authenticationRequest
                .flatMap(authRequest -> this.authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword()))
                        //.onErrorMap(BadCredentialsException.class, err -> new Throwable(HttpStatus.FORBIDDEN.toString()))
                        .map(this.jwtTokenProvider::createToken))
                .flatMap(jwt-> {
                    //HttpHeaders httpHeaders = new HttpHeaders();
                    //httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
                    var tokenBody = Map.of("access_token", jwt);
                    return ServerResponse
                            .ok()
                            .headers(httpHeaders1 -> httpHeaders1.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                            .bodyValue(tokenBody);

                });
    }
}
