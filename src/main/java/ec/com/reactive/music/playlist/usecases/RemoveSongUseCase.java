package ec.com.reactive.music.playlist.usecases;

import ec.com.reactive.music.playlist.dto.PlaylistDTO;
import ec.com.reactive.music.playlist.usecases.interfaces.GetPlaylistById;
import ec.com.reactive.music.playlist.usecases.interfaces.RemoveSong;
import ec.com.reactive.music.song.dto.SongDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RemoveSongUseCase implements RemoveSong {

    private final GetPlaylistById getPlaylistById;

    private final UpdatePlaylistUseCase updatePlaylistUseCase;

    @Override
    public Mono<PlaylistDTO> removeFromPlaylist(@PathVariable String playlistId, @RequestBody SongDTO songDTO) {
        return this.getPlaylistById
                .getPlaylist(playlistId)
                .flatMap(playlistDTO -> {
                    playlistDTO.getSongs().add(songDTO);
                    playlistDTO.setDuration(PlaylistDTO.removeDuration(playlistDTO.getDuration(),songDTO.getDuration()));
                    return this.updatePlaylistUseCase.applyUseCase(playlistId,playlistDTO);
                });
    }
}
