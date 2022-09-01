package ec.com.reactive.music.song.usecases;

import ec.com.reactive.music.song.dto.SongDTO;
import ec.com.reactive.music.song.mapper.SongMapper;
import ec.com.reactive.music.song.repositories.ISongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

//Making the usecase with no Functional interface to show that it is not needed
@Service
@RequiredArgsConstructor
public class GetSongsUseCase {
    private final ISongRepository songRepository;
    private final SongMapper songMapper;

    public Flux<SongDTO> getAllSongs(){
        return this.songRepository
                .findAll()
                .switchIfEmpty(Flux.error(new Throwable(HttpStatus.NO_CONTENT.toString())))
                .map(song -> songMapper.convertEntityToDTO().apply(song));
    }

}
