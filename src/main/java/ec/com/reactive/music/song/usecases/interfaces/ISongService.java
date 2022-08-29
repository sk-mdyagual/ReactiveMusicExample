package ec.com.reactive.music.song.usecases.interfaces;

import ec.com.reactive.music.song.collections.Song;
import ec.com.reactive.music.song.dto.SongDTO;
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
