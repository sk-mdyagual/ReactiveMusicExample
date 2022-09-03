package ec.com.reactive.music.song.usecases;

import ec.com.reactive.music.album.usecases.GetAlbumByIdUseCase;
import ec.com.reactive.music.song.dto.SongDTO;
import ec.com.reactive.music.song.mapper.SongMapper;
import ec.com.reactive.music.song.repositories.ISongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SaveSongUseCase {
    private final ISongRepository songRepository;
    private final GetAlbumByIdUseCase getAlbumByIdUseCase;
    private final SongMapper songMapper;

    public Mono<SongDTO> save(@RequestBody SongDTO songDTO){
        return getAlbumByIdUseCase.apply(songDTO.getIdAlbum())
                .flatMap(albumDTO ->  this.songRepository
                                .save(songMapper.convertDTOToEntity().apply(Objects.requireNonNull(songDTO)))
                                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_ACCEPTABLE.toString())))
                                .map(song -> songMapper.convertEntityToDTO().apply(song)));
        /*return this.songRepository
                .save(songMapper.convertDTOToEntity().apply(Objects.requireNonNull(songDTO)))
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_ACCEPTABLE.toString())))
                .map(song -> songMapper.convertEntityToDTO().apply(song));*/
    }
}
