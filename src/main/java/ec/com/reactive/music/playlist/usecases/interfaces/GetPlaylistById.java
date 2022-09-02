package ec.com.reactive.music.playlist.usecases.interfaces;

import ec.com.reactive.music.playlist.dto.PlaylistDTO;
import reactor.core.publisher.Mono;

public interface GetPlaylistById {
    Mono<PlaylistDTO> getPlaylist(String playlistId);
}
