package ec.com.reactive.music.song.usecases;

import ec.com.reactive.music.song.dto.SongDTO;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UpdateSongUseCaseTest {
    @Mock
    GetSongByIdUseCase useCaseGetById;

    @Mock
    SaveSongUseCase useCaseSave;

    UpdateSongUseCase useCase;

    @BeforeEach
    void init(){
        useCase = new UpdateSongUseCase(useCaseSave,useCaseGetById);
    }

    @Test
    @DisplayName("updateSongUseCase()")
    void updateSongUseCase(){
        var songDTO = new SongDTO("12345-6",
                "songTest",
                "65432-1",
                "lyricsTest",
                "producerTest",
                "arrangedTest",
                LocalTime.of(0,3,45));

        var songUpdated = songDTO.toBuilder().lyricsBy("Updated").build();

        Mockito.when(useCaseGetById.getSongById(Mockito.any(String.class))).thenReturn(Mono.just(songDTO));
        Mockito.when(useCaseSave.save(Mockito.any(SongDTO.class))).thenReturn(Mono.just(songUpdated));

        var useCaseExecute = useCase.update(songDTO.getIdSong(),songDTO);

        StepVerifier.create(useCaseExecute)
                .expectNext(songUpdated)
                .verifyComplete();

        Mockito.verify(useCaseGetById).getSongById(Mockito.any(String.class));
        Mockito.verify(useCaseSave).save(Mockito.any(SongDTO.class));




    }

}