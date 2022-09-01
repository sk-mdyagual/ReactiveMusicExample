package ec.com.reactive.music.album.usecases;

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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class SaveAlbumUseCaseTest {
    @Mock
    IAlbumRepository albumRepositoryMock;
    SaveAlbumUseCase useCase;
    AlbumMapper albumMapper;

    @BeforeEach
    void init(){
        albumMapper = new AlbumMapper(new ModelMapper());
        useCase = new SaveAlbumUseCase(albumRepositoryMock,albumMapper);

    }

    @Test
    @DisplayName("saveAlbumUseCase()")
    void saveAlbumUseCaseTest(){
        AlbumDTO albumExpected = new AlbumDTO();
        albumExpected.setIdAlbum("12345678-9");
        albumExpected.setName("albumTesting1");
        albumExpected.setArtist("testerArtist");
        albumExpected.setYearRelease(2015);

        var albumToBeSaved = albumExpected.toBuilder().idAlbum(null).build();
        Mockito.when(albumRepositoryMock
                .save(albumMapper.convertDTOToEntity().apply(albumToBeSaved)))
                .thenReturn(Mono.just(albumMapper.convertDTOToEntity().apply(albumExpected)));

        var useCaseExecute = useCase.applySaveAlbum(albumToBeSaved);
        StepVerifier.create(useCaseExecute)
                .expectNext(albumExpected)
                .expectComplete()
                .verify();
        Mockito.verify(albumRepositoryMock).save(albumMapper.convertDTOToEntity().apply(albumToBeSaved));
    }
}