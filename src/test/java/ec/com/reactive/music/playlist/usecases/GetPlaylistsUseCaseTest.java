package ec.com.reactive.music.playlist.usecases;

import ec.com.reactive.music.playlist.collection.Playlist;
import ec.com.reactive.music.playlist.dto.PlaylistDTO;
import ec.com.reactive.music.playlist.mapper.PlaylistMapper;
import ec.com.reactive.music.playlist.repository.IPlaylistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Objects;

@ExtendWith(MockitoExtension.class)
class GetPlaylistsUseCaseTest {
    @Mock
    IPlaylistRepository playlistRepositoryMock;

    PlaylistMapper playlistMapper;

    GetPlaylistsUseCase useCase;

    @BeforeEach
    void init(){
        playlistMapper = new PlaylistMapper(new ModelMapper());
        useCase = new GetPlaylistsUseCase(playlistRepositoryMock,playlistMapper);
    }

    @Test
    @DisplayName("getAllPlaylists()")
    void getPlaylistsUseCase(){
        Flux<Playlist> fluxResult = Flux.just(new Playlist(),new Playlist());

        Mockito.when(playlistRepositoryMock.findAll()).thenReturn(fluxResult);

        var useCaseExecute = useCase.applyUseCase();

        StepVerifier.create(useCaseExecute)
                .expectNextMatches(Objects::nonNull)
                .expectNextCount(1).verifyComplete();

        Mockito.verify(playlistRepositoryMock).findAll();

    }

}