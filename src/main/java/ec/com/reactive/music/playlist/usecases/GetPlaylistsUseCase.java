package ec.com.reactive.music.playlist.usecases;

import ec.com.reactive.music.playlist.dto.PlaylistDTO;
import ec.com.reactive.music.playlist.mapper.PlaylistMapper;
import ec.com.reactive.music.playlist.repository.IPlaylistRepository;
import ec.com.reactive.music.playlist.usecases.interfaces.GetPlaylists;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class GetPlaylistsUseCase implements GetPlaylists {
    private final IPlaylistRepository playlistRepository;
    private final PlaylistMapper playlistMapper;

    @Override
    public Flux<PlaylistDTO> applyUseCase() {
        return this.playlistRepository
                .findAll()
                .switchIfEmpty(Flux.error(new Throwable(HttpStatus.NO_CONTENT.toString())))
                .map(playlist -> playlistMapper.convertEntityToDTO().apply(playlist));
    }
}
