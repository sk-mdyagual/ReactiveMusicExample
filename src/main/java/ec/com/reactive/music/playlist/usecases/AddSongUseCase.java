package ec.com.reactive.music.playlist.usecases;

import ec.com.reactive.music.playlist.dto.PlaylistDTO;
import ec.com.reactive.music.playlist.usecases.interfaces.AddSong;
import ec.com.reactive.music.playlist.usecases.interfaces.GetPlaylistById;
import ec.com.reactive.music.song.dto.SongDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AddSongUseCase implements AddSong {
    private final GetPlaylistById getPlaylistById;

    private final UpdatePlaylistUseCase updatePlaylistUseCase;

    @Override
    public Mono<PlaylistDTO> addToPlaylist(String playlistId, SongDTO songDTO) {
        return this.getPlaylistById
                .getPlaylist(playlistId)
                .flatMap(playlistDTO -> {
                    playlistDTO.getSongs().add(songDTO);
                    playlistDTO.setDuration(PlaylistDTO.addDuration(playlistDTO.getDuration(),songDTO.getDuration()));
                    return this.updatePlaylistUseCase.applyUseCase(playlistId,playlistDTO);
                });
    }


}