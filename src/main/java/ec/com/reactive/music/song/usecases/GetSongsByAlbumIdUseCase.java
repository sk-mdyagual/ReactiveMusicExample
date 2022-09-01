package ec.com.reactive.music.song.usecases;

import ec.com.reactive.music.album.mapper.AlbumMapper;
import ec.com.reactive.music.album.usecases.GetAlbumByIdUseCase;
import ec.com.reactive.music.song.dto.SongDTO;
import ec.com.reactive.music.song.repositories.ISongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class GetSongsByAlbumIdUseCase {
    public final GetAlbumByIdUseCase getAlbumByIdUseCase;
    public final GetSongsUseCase getSongsUseCase;

    public Flux<SongDTO> byAlbumId(String albumId){
        return getAlbumByIdUseCase.apply(albumId)
                .flatMapMany(albumDTO -> getSongsUseCase
                        .getAllSongs()
                        .filter(songDTO -> songDTO.getIdAlbum().equals(albumDTO.getIdAlbum())));
    }
}
