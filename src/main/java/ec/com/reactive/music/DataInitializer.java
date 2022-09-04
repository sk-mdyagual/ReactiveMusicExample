package ec.com.reactive.music;

import ec.com.reactive.music.user.collection.User;
import ec.com.reactive.music.user.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final IUserRepository users;

    private final PasswordEncoder passwordEncoder;

    @EventListener(value = ApplicationReadyEvent.class)
    public void init() {
        Logger.getLogger("start data initialization...");
        var initPosts = this.users.deleteAll()
                .thenMany(
                        Flux.just("user", "admin")
                                .flatMap(username -> {
                                    List<String> roles = "user".equals(username) ?
                                            Arrays.asList("ROLE_USER") : Arrays.asList("ROLE_USER", "ROLE_ADMIN");

                                    User user = User.builder()
                                            .roles(roles)
                                            .username(username)
                                            .password(passwordEncoder.encode("password"))
                                            .email(username + "@example.com")
                                            .build();

                                    return this.users.save(user);
                                })
                );

        initPosts.doOnSubscribe(data -> System.out.println("data:" + data))
                .subscribe(
                        data -> System.out.println("data:" + data), err -> System.out.println("error:" + err),
                        () -> System.out.println("done initialization...")
                );


    }

}
