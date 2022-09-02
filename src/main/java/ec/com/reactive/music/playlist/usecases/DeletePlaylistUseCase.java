package ec.com.reactive.music.playlist.usecases;

import ec.com.reactive.music.playlist.mapper.PlaylistMapper;
import ec.com.reactive.music.playlist.repository.IPlaylistRepository;
import ec.com.reactive.music.playlist.usecases.interfaces.DeletePlaylist;
import ec.com.reactive.music.playlist.usecases.interfaces.GetPlaylistById;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DeletePlaylistUseCase implements DeletePlaylist {
    private final GetPlaylistById getPlaylistById;

    private final IPlaylistRepository playlistRepository;

    private final PlaylistMapper playlistMapper;

    @Override
    public Mono<String> applyUseCase(String playlistId) {
        return this.getPlaylistById
                .getPlaylist(playlistId)
                .map(playlistDTO -> {
                    this.playlistRepository.delete(playlistMapper.convertDTOToEntity().apply(playlistDTO));
                    return playlistDTO.getIdPlaylist();
                });
    }
}
