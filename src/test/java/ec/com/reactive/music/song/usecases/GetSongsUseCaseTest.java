package ec.com.reactive.music.song.usecases;

import ec.com.reactive.music.song.dto.SongDTO;
import ec.com.reactive.music.song.mapper.SongMapper;
import ec.com.reactive.music.song.repositories.ISongRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GetSongsUseCaseTest {
    @Mock
    ISongRepository songRepositoryMock;
    SongMapper songMapper;
    GetSongsUseCase useCase;


    @BeforeEach
    void init(){
        songMapper = new SongMapper(new ModelMapper());
        useCase = new GetSongsUseCase(songRepositoryMock,songMapper);

    }

    @Test
    void getAllSongs() {
        var fluxExpected = Flux.just(new SongDTO(),new SongDTO());

        Mockito.when(songRepositoryMock.findAll()).thenReturn(fluxExpected.map(songDTO -> songMapper.convertDTOToEntity().apply(songDTO)));

        var useCaseExecute = useCase.getAllSongs();

        StepVerifier.create(useCaseExecute)
                .expectNextMatches(Objects::nonNull)
                .expectNextCount(1)
                .verifyComplete();

        Mockito.verify(songRepositoryMock).findAll();
    }
}