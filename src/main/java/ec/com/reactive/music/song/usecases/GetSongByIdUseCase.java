package ec.com.reactive.music.song.usecases;

import ec.com.reactive.music.song.dto.SongDTO;
import ec.com.reactive.music.song.mapper.SongMapper;
import ec.com.reactive.music.song.repositories.ISongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

//Making the usecase with no Functional interface to show that it is not needed
@Service
@RequiredArgsConstructor
public class GetSongByIdUseCase {
    private final ISongRepository songRepository;
    private final SongMapper songMapper;

    public Mono<SongDTO> getSongById(String songId){
        return this.songRepository
                .findById(songId)
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())))
                .map(song -> songMapper.convertEntityToDTO().apply(song));
    }
}
