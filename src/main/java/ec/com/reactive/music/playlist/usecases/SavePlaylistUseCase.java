package ec.com.reactive.music.playlist.usecases;

import ec.com.reactive.music.playlist.dto.PlaylistDTO;
import ec.com.reactive.music.playlist.mapper.PlaylistMapper;
import ec.com.reactive.music.playlist.repository.IPlaylistRepository;
import ec.com.reactive.music.playlist.usecases.interfaces.SavePlaylist;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SavePlaylistUseCase implements SavePlaylist {

    private final IPlaylistRepository playlistRepository;
    private final PlaylistMapper playlistMapper;

    @Override
    public Mono<PlaylistDTO> applyUseCase(@RequestBody PlaylistDTO playlistDTO) {
        return this.playlistRepository.save(playlistMapper.convertDTOToEntity().apply(Objects.requireNonNull(playlistDTO)))
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_ACCEPTABLE.toString())))
                .map(playlist -> playlistMapper.convertEntityToDTO().apply(playlist));
    }
}
