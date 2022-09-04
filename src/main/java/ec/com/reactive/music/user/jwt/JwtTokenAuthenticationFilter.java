package ec.com.reactive.music.user.jwt;

import ec.com.reactive.music.user.collection.AuthenticationRequest;
import ec.com.reactive.music.user.collection.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Objects;

@RequiredArgsConstructor
public class JwtTokenAuthenticationFilter implements WebFilter {
    public static final String HEADER_PREFIX = "Bearer ";

    private final JwtTokenProvider tokenProvider;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = resolveToken(exchange.getRequest());
        if (StringUtils.hasText(token) && this.tokenProvider.validateToken(token)) {

            /*return ReactiveSecurityContextHolder.getContext()
                    .map(securityContext -> ((Principal) securityContext.getAuthentication().getPrincipal()).getName())
                    .then(chain.filter(exchange));*/
            Authentication authentication = this.tokenProvider.getAuthentication(token);
            return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
            /*Mono<SecurityContext> context  = ReactiveSecurityContextHolder.getContext();
            return context.filter(c -> Objects.nonNull(c.getAuthentication()))
                    .map(s -> s.getAuthentication().getPrincipal())
                    .then(chain.filter(exchange));*/
            /*ReactiveSecurityContextHolder.getContext()
                    .map(context -> context.getAuthentication().getPrincipal())
                    .cast(UserDetails.class)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
                    .subscribe();*/

        }
        return chain.filter(exchange);
    }

    private String resolveToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
