package ec.com.reactive.music.song.usecases;

import ec.com.reactive.music.song.dto.SongDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UpdateSongUseCase {
    private final SaveSongUseCase saveSongUseCase;
    private final GetSongByIdUseCase getSongByIdUseCase;

    public Mono<SongDTO> update(@PathVariable String songId, @RequestBody SongDTO songDTO){
        return getSongByIdUseCase.getSongById(songId)
                .flatMap(songDTOFound -> {
                    Objects.requireNonNull(songDTO).setIdSong(songDTOFound.getIdSong());
                    return saveSongUseCase.save(songDTO);
                });
    }
}
