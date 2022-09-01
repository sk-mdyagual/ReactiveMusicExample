package ec.com.reactive.music.song.usecases;

import ec.com.reactive.music.song.dto.SongDTO;
import ec.com.reactive.music.song.mapper.SongMapper;
import ec.com.reactive.music.song.repositories.ISongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SaveSongUseCase {
    private final ISongRepository songRepository;
    private final SongMapper songMapper;

    public Mono<SongDTO> save(SongDTO songDTO){
        return this.songRepository
                .save(songMapper.convertDTOToEntity().apply(Objects.requireNonNull(songDTO)))
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_ACCEPTABLE.toString())))
                .map(song -> songMapper.convertEntityToDTO().apply(song));
    }
}
