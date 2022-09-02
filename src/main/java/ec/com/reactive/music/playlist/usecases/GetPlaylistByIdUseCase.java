package ec.com.reactive.music.playlist.usecases;

import ec.com.reactive.music.playlist.dto.PlaylistDTO;
import ec.com.reactive.music.playlist.mapper.PlaylistMapper;
import ec.com.reactive.music.playlist.repository.IPlaylistRepository;
import ec.com.reactive.music.playlist.usecases.interfaces.GetPlaylistById;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GetPlaylistByIdUseCase implements GetPlaylistById {
    private final IPlaylistRepository playlistRepository;

    private final PlaylistMapper playlistMapper;


    @Override
    public Mono<PlaylistDTO> getPlaylist(String playlistId) {
        return this.playlistRepository
                .findById(playlistId)
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())))
                .map(playlist -> playlistMapper.convertEntityToDTO().apply(playlist));
    }
}
