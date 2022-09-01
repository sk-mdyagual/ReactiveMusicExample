package ec.com.reactive.music.song.usecases;

import ec.com.reactive.music.album.mapper.AlbumMapper;
import ec.com.reactive.music.song.repositories.ISongRepository;
import ec.com.reactive.music.song.usecases.interfaces.DeleteSong;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
/*In order to check a deleteUseCase I did it for Song entity
* As a personal practice with delete operations i like to return something, and that will be the id*/
@Service
@RequiredArgsConstructor
public class DeleteSongByIdUseCase implements DeleteSong {

    private final ISongRepository songRepository;

    @Override
    public Mono<String> apply(String id) {
        return this.songRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())))
                .flatMap(this.songRepository::delete)
                .thenReturn(id);
    }
}
