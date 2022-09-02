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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import javax.print.DocFlavor;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DeletePlaylistUseCaseTest {
    @Mock
    IPlaylistRepository playlistRepositoryMock;

    @Mock
    GetPlaylistByIdUseCase useCaseGetById;

    PlaylistMapper playlistMapper;

    DeletePlaylistUseCase useCase;

    @BeforeEach
    void init(){
        playlistMapper = new PlaylistMapper(new ModelMapper());
        useCase = new DeletePlaylistUseCase(useCaseGetById,playlistRepositoryMock,playlistMapper);


    }

    @Test
    @DisplayName("deleteById()")
    void deletePlaylistByIdUseCase(){
        var playlistFound = new PlaylistDTO("1234-5", "PlaylistTest","usernameTest",new ArrayList<>(), LocalTime.of(0,0,0));

        Mockito.when(useCaseGetById.getPlaylist(Mockito.any(String.class))).thenReturn(Mono.just(playlistFound));
        Mockito.when(playlistRepositoryMock.delete(playlistMapper.convertDTOToEntity().apply(playlistFound))).thenReturn(Mono.empty());

        var useCaseExecute = useCase.applyUseCase(playlistFound.getIdPlaylist());

        StepVerifier.create(useCaseExecute)
                .expectNext(playlistFound.getIdPlaylist())
                .verifyComplete();

        Mockito.verify(useCaseGetById).getPlaylist(Mockito.any(String.class));
        Mockito.verify(playlistRepositoryMock).delete(Mockito.any(Playlist.class));

    }

}