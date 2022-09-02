package ec.com.reactive.music.song.usecases;

import ec.com.reactive.music.song.dto.SongDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UpdateSongUseCase {
    private final SaveSongUseCase saveSongUseCase;
    private final GetSongByIdUseCase getSongByIdUseCase;

    public Mono<SongDTO> update(String songId, SongDTO songDTO){
        return getSongByIdUseCase.getSongById(songId)
                .flatMap(songDTOFound -> {
                    Objects.requireNonNull(songDTO).setIdSong(songDTOFound.getIdSong());
                    return saveSongUseCase.save(songDTO);
                });
    }
}
