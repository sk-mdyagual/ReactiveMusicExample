package ec.com.reactive.music.album.usecases;

import ec.com.reactive.music.album.collection.Album;
import ec.com.reactive.music.album.mapper.AlbumMapper;
import ec.com.reactive.music.album.repository.IAlbumRepository;
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

@ExtendWith(MockitoExtension.class)
class GetAlbumByIdUseCaseTest {

    @Mock
    IAlbumRepository albumRepositoryMock;
    GetAlbumByIdUseCase useCase;
    AlbumMapper albumMapper;

    @BeforeEach
    void init(){
        albumMapper = new AlbumMapper(new ModelMapper());
        useCase = new GetAlbumByIdUseCase(albumRepositoryMock,albumMapper);

    }

    @Test
    @DisplayName("getAlbumByIdUseCase()")
    void getAlbumByIdUseCaseTest(){
        Album albumExpected = new Album();
        albumExpected.setIdAlbum("12345678-9");
        albumExpected.setName("albumTesting1");
        albumExpected.setArtist("testerArtist");
        albumExpected.setYearRelease(2015);

        var albumDTOExpected = albumMapper.convertEntityToDTO().apply(albumExpected);

        Mockito.when(albumRepositoryMock.findById(Mockito.any(String.class))).thenReturn(Mono.just(albumExpected));

        var useCaseExecute = useCase.apply(albumExpected.getIdAlbum());

        StepVerifier.create(useCaseExecute)
                .expectNext(albumDTOExpected)
                .expectComplete()
                .verify();

        Mockito.verify(albumRepositoryMock).findById(albumExpected.getIdAlbum());
    }



}