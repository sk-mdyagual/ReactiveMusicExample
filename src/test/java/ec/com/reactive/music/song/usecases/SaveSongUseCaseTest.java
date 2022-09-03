package ec.com.reactive.music.song.usecases;

import ec.com.reactive.music.album.dto.AlbumDTO;
import ec.com.reactive.music.album.usecases.GetAlbumByIdUseCase;
import ec.com.reactive.music.song.collections.Song;
import ec.com.reactive.music.song.dto.SongDTO;
import ec.com.reactive.music.song.mapper.SongMapper;
import ec.com.reactive.music.song.repositories.ISongRepository;
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

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SaveSongUseCaseTest {
    @Mock
    ISongRepository songRepositoryMock;

    @Mock
    GetAlbumByIdUseCase useCaseAlbumById;

    SongMapper songMapper;
    SaveSongUseCase useCase;

    @BeforeEach
    void init(){
        songMapper = new SongMapper(new ModelMapper());
        useCase = new SaveSongUseCase(songRepositoryMock,useCaseAlbumById,songMapper);
    }

    @Test
    @DisplayName("saveSongUseCase()")
    void save() {
        var albumFounded = new AlbumDTO("1234-5","albumTest","artistTest",2020);
        var songToSave = new SongDTO(null,"songTest","1234-5","lyricsTest","producedTest","arrangeTest", LocalTime.of(0,3,45));
        var songSaved = songToSave.toBuilder().idSong("321-5").build();

        Mockito.when(useCaseAlbumById.apply(Mockito.any(String.class))).thenReturn(Mono.just(albumFounded));
        Mockito.when(songRepositoryMock.save(Mockito.any(Song.class))).thenReturn(Mono.just(songMapper.convertDTOToEntity().apply(songSaved)));

        var useCaseExecute=useCase.save(songToSave);

        StepVerifier.create(useCaseExecute)
                .expectNext(songSaved)
                .verifyComplete();

        Mockito.verify(useCaseAlbumById).apply(Mockito.any(String.class));
        Mockito.verify(songRepositoryMock).save(Mockito.any(Song.class));
    }
}