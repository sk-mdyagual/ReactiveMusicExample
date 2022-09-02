package ec.com.reactive.music.playlist.usecases.interfaces;

import ec.com.reactive.music.playlist.dto.PlaylistDTO;
import ec.com.reactive.music.song.dto.SongDTO;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface RemoveSong {
    Mono<PlaylistDTO> removeFromPlaylist(String playlistId, SongDTO songDTO);
}
