package ec.com.reactive.music.user.usecases;

import ec.com.reactive.music.user.collection.User;
import ec.com.reactive.music.user.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CreateUserUseCase {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public Mono<User> save (User u,String role){
       return this.userRepository
               .save(u.toBuilder()
                       .password(passwordEncoder.encode(u.getPassword()))
                       .email(u.getUsername() + "@example.com")
                       .roles(new ArrayList<>(){ {add(role);}}).build());

    }
}
