package ec.com.reactive.music.service;

import ec.com.reactive.music.domain.dto.PlaylistDTO;
import ec.com.reactive.music.domain.dto.SongDTO;
import ec.com.reactive.music.domain.entities.Playlist;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IPlaylistService {
    Mono<ResponseEntity<Flux<PlaylistDTO>>> findPlaylists();
    Mono<ResponseEntity<PlaylistDTO>> findPlaylistById(String id);
    Mono<ResponseEntity<PlaylistDTO>> savePlaylist(PlaylistDTO pDto);
    Mono<ResponseEntity<PlaylistDTO>> updatePlaylist(String id, PlaylistDTO pDto);
    Mono<ResponseEntity<PlaylistDTO>> addSongPlaylist(String idAlbum, SongDTO sDto);

    Mono<ResponseEntity<PlaylistDTO>> removeSongPlaylist(String idPlaylist, SongDTO sDto);
    Mono<ResponseEntity<String>> deletePlaylist(String id);

    PlaylistDTO entityToDTO(Playlist p);
    Playlist dtoToEntity(PlaylistDTO pDto);




}
