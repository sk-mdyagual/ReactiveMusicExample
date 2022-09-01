package ec.com.reactive.music.song.usecases;

import ec.com.reactive.music.song.dto.SongDTO;
import ec.com.reactive.music.song.repositories.ISongRepository;
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

@ExtendWith(MockitoExtension.class)
class DeleteSongByIdUseCaseTest {
    @Mock
    ISongRepository songRepositoryMock;
    @Mock
    GetSongByIdUseCase getSongByIdUseCaseMock;

    DeleteSongByIdUseCase useCase;

    @BeforeEach
    void init(){
        useCase = new DeleteSongByIdUseCase(songRepositoryMock,getSongByIdUseCaseMock);
    }

    @Test
    @DisplayName("DeleteSongByIdUseCase")
    void deleteSongByIdUseCase(){
        SongDTO songDTOFound = new SongDTO("12345-6","songTest","65432-1","lyricsTest","producerTest","arrangedTest", LocalTime.of(0,3,45));

        Mockito.when(getSongByIdUseCaseMock.getSongById(Mockito.any(String.class))).thenReturn(Mono.just(songDTOFound));
        Mockito.when(songRepositoryMock.deleteById(Mockito.any(String.class))).thenReturn(Mono.empty());

        var idSent = songDTOFound.getIdSong();

        var useCaseExecute = useCase.apply(songDTOFound.getIdSong());

        StepVerifier.create(useCaseExecute).expectNext(idSent)
                        .expectComplete().verify();

        Mockito.verify(getSongByIdUseCaseMock).getSongById(Mockito.any(String.class));
        Mockito.verify(songRepositoryMock).deleteById(Mockito.any(String.class));

    }
}