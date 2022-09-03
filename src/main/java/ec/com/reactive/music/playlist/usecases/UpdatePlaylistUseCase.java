package ec.com.reactive.music.playlist.usecases;

import ec.com.reactive.music.playlist.dto.PlaylistDTO;
import ec.com.reactive.music.playlist.usecases.interfaces.GetPlaylistById;
import ec.com.reactive.music.playlist.usecases.interfaces.SavePlaylist;
import ec.com.reactive.music.playlist.usecases.interfaces.UpdatePlaylist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UpdatePlaylistUseCase implements UpdatePlaylist {

    private final GetPlaylistById getPlaylistById;

    private final SavePlaylistUseCase savePlaylistUseCase;

    @Override
    public Mono<PlaylistDTO> applyUseCase(@PathVariable String playlistId, @RequestBody PlaylistDTO playlistDTO) {
        return this.getPlaylistById
                .getPlaylist(playlistId)
                .flatMap(playlistDTO1 -> {
                    playlistDTO.setIdPlaylist(playlistDTO1.getIdPlaylist());
                    return this.savePlaylistUseCase.applyUseCase(playlistDTO);
                });
    }
}
