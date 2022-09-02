package ec.com.reactive.music.album.usecases;

import ec.com.reactive.music.album.dto.AlbumDTO;
import ec.com.reactive.music.album.repository.IAlbumRepository;
import ec.com.reactive.music.album.usecases.interfaces.SaveAlbum;
import ec.com.reactive.music.album.mapper.AlbumMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SaveAlbumUseCase implements SaveAlbum {

    private final IAlbumRepository albumRepository;

    private final AlbumMapper albumMapper;

    /*I update it in order to manage scenarios when we sent an empty AlbumDTO object or some of the attributes is null
    * Using the ternary operator I split both cases. On the router, I will manage the catch of the exception dropped by
    * switchIfEmpty also the Mono.errors()*/
    @Override
    public Mono<AlbumDTO> applySaveAlbum(AlbumDTO albumDTO) {
        return !Objects.isNull(albumDTO)? //A static method in final class Objects that allows to check if albumDTO is null
                !AlbumDTO.thereIsNullAttributes().test(albumDTO) ? //This is a predicate, I create it on AlbumDTO class
                this.albumRepository
                        .save(albumMapper.convertDTOToEntity().apply(albumDTO))
                        .switchIfEmpty(Mono.error(new Throwable(HttpStatus.EXPECTATION_FAILED.toString())))
                        .map(album -> albumMapper.convertEntityToDTO().apply(album))
                : Mono.error(new Throwable(HttpStatus.NOT_ACCEPTABLE.toString()))
        : Mono.error(new Throwable(HttpStatus.NOT_ACCEPTABLE.toString()));

    }



}
