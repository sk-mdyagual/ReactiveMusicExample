package ec.com.reactive.music.album.usecases;

import ec.com.reactive.music.album.dto.AlbumDTO;
import ec.com.reactive.music.album.repository.IAlbumRepository;
import ec.com.reactive.music.album.usecases.interfaces.SaveAlbum;
import ec.com.reactive.music.album.mapper.AlbumMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SaveAlbumUseCase implements SaveAlbum {

    @Autowired
    private IAlbumRepository albumRepository;

    @Autowired
    private AlbumMapper albumMapper;

    @Override
    public Mono<AlbumDTO> applySaveAlbum(AlbumDTO albumDTO) {
        return this.albumRepository
                .save(albumMapper.convertDTOToEntity().apply(albumDTO))
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.EXPECTATION_FAILED.toString())))
                .map(album -> albumMapper.convertEntityToDTO().apply(album))
                .onErrorResume(throwable -> {
                    //albumDTO.setIdAlbum(throwable.getCause().toString());
                    return Mono.just(albumDTO.toBuilder().idAlbum(null).build());
                });
    }



}
