package ec.com.reactive.music.user.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CurrentUserControllerUseCase {
    public Mono<Map<String, Object>> current(@AuthenticationPrincipal Mono<UserDetails> principal) {
        System.out.println(principal);
        return principal.map(user -> Map.of(
                        "name", user.getUsername(),
                        "roles", AuthorityUtils.authorityListToSet(user.getAuthorities())
                )
        );
    }
}
