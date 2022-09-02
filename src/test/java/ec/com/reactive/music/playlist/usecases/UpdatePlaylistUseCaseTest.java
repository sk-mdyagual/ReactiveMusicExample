package ec.com.reactive.music.playlist.usecases;

import ec.com.reactive.music.album.usecases.UpdateAlbumUseCase;
import ec.com.reactive.music.playlist.dto.PlaylistDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UpdatePlaylistUseCaseTest {
    @Mock
    GetPlaylistByIdUseCase useCaseGetById;

    @Mock
    SavePlaylistUseCase useCaseSave;

    UpdatePlaylistUseCase useCase;

    @BeforeEach
    void init(){
        useCase = new UpdatePlaylistUseCase(useCaseGetById,useCaseSave);
    }

    @Test
    @DisplayName("updatePlaylistUseCase()")
    void updatePlaylistUseCase(){
        var playlistFound = new PlaylistDTO("1234-5", "PlaylistTest","usernameTest",new ArrayList<>(),LocalTime.of(0,0,0));
        var playlistUpdated = playlistFound.toBuilder().name("EditedPlaylistTest").build();

        Mockito.when(useCaseGetById.getPlaylist(Mockito.any(String.class))).thenReturn(Mono.just(playlistFound));
        Mockito.when(useCaseSave.applyUseCase(Mockito.any(PlaylistDTO.class))).thenReturn(Mono.just(playlistUpdated));

        var useCaseExecute = useCase.applyUseCase("1234-5",playlistUpdated);

        StepVerifier.create(useCaseExecute)
                .expectNext(playlistUpdated)
                .verifyComplete();

        Mockito.verify(useCaseGetById).getPlaylist("1234-5");
        Mockito.verify(useCaseSave).applyUseCase(playlistUpdated);
    }

}