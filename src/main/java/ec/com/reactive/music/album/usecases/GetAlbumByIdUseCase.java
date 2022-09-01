package ec.com.reactive.music.album.usecases;

import ec.com.reactive.music.album.collection.Album;
import ec.com.reactive.music.album.dto.AlbumDTO;
import ec.com.reactive.music.album.mapper.AlbumMapper;
import ec.com.reactive.music.album.repository.IAlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Function;
/*Remember our Functional Interface Function? Well, Function always receive something and returns another,
 * so this is the expected behavior that we need in this usecase. Also remember that to activate a Function
 * you use the apply() method. So, when you do the implementation you will override that method and set up inside what
 * do you want to truly implement.*/
@Service
@RequiredArgsConstructor
public class GetAlbumByIdUseCase implements Function<String, Mono<AlbumDTO>> {

    //Don't forget our default initializations: AlbumMapper and IAlbumRepository
    //Why we are not using @Autowired?
    private final IAlbumRepository albumRepository;

    private final AlbumMapper albumMapper;

    /*From now we will treat the Mono.error() on the respective route in AlbumRouter. So, let focus only in
    * launch then alongside with the switchIfEmpty()*/
    @Override
    public Mono<AlbumDTO> apply(String s) {
        return albumRepository
                .findById(s)
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())))
                .map(album -> albumMapper.convertEntityToDTO().apply(album));
    }
}
