package ec.com.reactive.music.song.usecases;

import ec.com.reactive.music.album.collection.Album;
import ec.com.reactive.music.album.dto.AlbumDTO;
import ec.com.reactive.music.album.usecases.GetAlbumByIdUseCase;
import ec.com.reactive.music.song.dto.SongDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GetSongsByAlbumIdUseCaseTest {
    @Mock
    GetAlbumByIdUseCase useCaseAlbumById;

    @Mock
    GetSongsUseCase useCaseAllSongs;

    GetSongsByAlbumIdUseCase useCase;

    @BeforeEach
    void init(){
        useCase = new GetSongsByAlbumIdUseCase(useCaseAlbumById,useCaseAllSongs);
    }

    @Test
    @DisplayName("getSongsByAlbumId()")
    void byAlbumId() {
        var albumExpected = new AlbumDTO();
        albumExpected.setIdAlbum("65432-1");
        albumExpected.setName("albumTesting1");
        albumExpected.setArtist("testerArtist");
        albumExpected.setYearRelease(2015);

        var songDTOFound1 = new SongDTO("12345-6",
                "songTest",
                "65432-1",
                "lyricsTest",
                "producerTest",
                "arrangedTest",
                LocalTime.of(0,3,45));

        var songDTOFound2 = new SongDTO("12345-6",
                "songTest2",
                "65432-1",
                "lyricsTest2",
                "producerTest2",
                "arrangedTest2",
                LocalTime.of(0,3,45));

        var fluxAllSongs = Flux.just(songDTOFound1,songDTOFound2,songDTOFound1.toBuilder().idAlbum("222222").build());
        var fluxSongsExpected = Flux.just(songDTOFound1,songDTOFound2);

        Mockito.when(useCaseAlbumById.apply(Mockito.any(String.class))).thenReturn(Mono.just(albumExpected));
        Mockito.when(useCaseAllSongs.getAllSongs()).thenReturn(fluxAllSongs);

        var useCaseExecute = useCase.byAlbumId("12345-6");

        StepVerifier.create(useCaseExecute)
                .expectNext(songDTOFound1)
                .expectNext(songDTOFound2)
                .verifyComplete();

        Mockito.verify(useCaseAlbumById).apply(Mockito.any(String.class));
        Mockito.verify(useCaseAllSongs).getAllSongs();
    }
}