package ec.com.reactive.music.album.usecases;

import ec.com.reactive.music.album.dto.AlbumDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class UpdateAlbumUseCaseTest {

    @Mock
    GetAlbumByIdUseCase useCaseFindByIdMock;

    @Mock
    SaveAlbumUseCase saveAlbumUseCaseMock;

    UpdateAlbumUseCase useCaseUpdate;


    @BeforeEach
    void init(){
        useCaseUpdate = new UpdateAlbumUseCase(useCaseFindByIdMock,saveAlbumUseCaseMock);
    }

    @Test
    @DisplayName("updateAlbumUseCase()")
    void updateAlbumUseCase(){
        AlbumDTO albumExpected = new AlbumDTO();
        albumExpected.setIdAlbum("12345678-9");
        albumExpected.setName("albumTesting1");
        albumExpected.setArtist("testerArtist");
        albumExpected.setYearRelease(2015);

        var albumEdited = albumExpected.toBuilder().name("Edited").build();

        Mockito.when(useCaseFindByIdMock.apply(Mockito.any(String.class))).thenReturn(Mono.just(albumExpected));
        Mockito.when(saveAlbumUseCaseMock.applySaveAlbum(Mockito.any(AlbumDTO.class))).thenReturn(Mono.just(albumEdited));

        var useCaseExecute = useCaseUpdate.applyUpdateAlbum("12345678-9",albumEdited);

        StepVerifier.create(useCaseExecute)
                .expectNext(albumEdited)
                .expectComplete()
                .verify();


        Mockito.verify(useCaseFindByIdMock).apply("12345678-9");
        Mockito.verify(saveAlbumUseCaseMock).applySaveAlbum(albumEdited);
    }


}