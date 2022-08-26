package ec.com.reactive.music.service;

import ec.com.reactive.music.domain.dto.SongDTO;
import ec.com.reactive.music.domain.entities.Song;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ISongService {

    Mono<ResponseEntity<Flux<SongDTO>>> findAllSongs();
    Mono<ResponseEntity<SongDTO>> findSongById(String id);
    Mono<ResponseEntity<SongDTO>> saveSong (SongDTO s);
    Mono<ResponseEntity<SongDTO>> updateSong(String idSong, SongDTO sDto);
    Mono<ResponseEntity<String>> deleteSong (String idSong);

    SongDTO entityToDTO(Song s);
    Song dtoToEntity(SongDTO sDto);
}
