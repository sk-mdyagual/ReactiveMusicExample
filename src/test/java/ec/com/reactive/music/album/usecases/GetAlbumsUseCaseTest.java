package ec.com.reactive.music.album.usecases;

import ec.com.reactive.music.album.collection.Album;
import ec.com.reactive.music.album.dto.AlbumDTO;
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
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Objects;

@ExtendWith(MockitoExtension.class)
class GetAlbumsUseCaseTest {
    @Mock
    IAlbumRepository albumRepositoryMock;
    GetAlbumsUseCase useCase;
    AlbumMapper albumMapper;

    @BeforeEach
    void init(){
        albumMapper = new AlbumMapper(new ModelMapper());
        useCase = new GetAlbumsUseCase(albumRepositoryMock,albumMapper);

    }

    @Test
    @DisplayName("getAlbumsUseCase()")
    void getAlbumsUseCaseTest(){
        Flux<Album> albumsExpected2 = Flux.just(new Album(),new Album());

        Mockito.when(albumRepositoryMock.findAll()).thenReturn(albumsExpected2);

        var useCaseExecute = useCase.get();

        StepVerifier.create(useCaseExecute)
                .expectNextMatches(Objects::nonNull)
                        .expectNextCount(1).verifyComplete();

        Mockito.verify(albumRepositoryMock).findAll();

    }

}