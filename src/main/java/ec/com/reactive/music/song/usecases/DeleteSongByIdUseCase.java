package ec.com.reactive.music.song.usecases;

import ec.com.reactive.music.song.mapper.SongMapper;
import ec.com.reactive.music.song.repositories.ISongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/*In order to check a deleteUseCase I did it for Song entity
* As a personal practice with delete operations i like to return something, and that will be the id*/
@Service
@RequiredArgsConstructor
public class DeleteSongByIdUseCase {

    private final ISongRepository songRepository;

    private final GetSongByIdUseCase getSongByIdUseCase;

    private final SongMapper songMapper;


    public Mono<String> apply(String id) {
        return getSongByIdUseCase.getSongById(id)
                .flatMap(songDTO -> this.songRepository.delete(songMapper.convertDTOToEntity().apply(songDTO)))
                .map(result -> id);
    }
}
