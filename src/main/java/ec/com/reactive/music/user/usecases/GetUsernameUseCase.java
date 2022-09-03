package ec.com.reactive.music.user.usecases;

import ec.com.reactive.music.user.collection.User;
import ec.com.reactive.music.user.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GetUsernameUseCase {
    private final IUserRepository userRepository;

    public Mono<User> getUser(String username){
        return this.userRepository.findByUsername(username);
    }
}
