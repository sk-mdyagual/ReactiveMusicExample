package ec.com.reactive.music.album.usecases;

import ec.com.reactive.music.album.dto.AlbumDTO;
import ec.com.reactive.music.album.usecases.interfaces.UpdateAlbum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import java.util.Objects;


/*For update use case i create my own interface but it is possible to use a Function too as in GetAlbumByIdUseCase
* because you will send something and return another something*/
@Service
@RequiredArgsConstructor
public class UpdateAlbumUseCase implements UpdateAlbum {

    //private final IAlbumRepository albumRepository;

    private final GetAlbumByIdUseCase getAlbumByIdUseCase;

    private final SaveAlbumUseCase saveAlbumUseCase;

    //private final AlbumMapper albumMapper;

    /*I'm considering again the scenarios when we sent an empty AlbumDTO object or some of the attributes is null
     * Using the ternary operator I split both cases. On the router, I will manage the catch of the exception dropped by
     * switchIfEmpty also the Mono.errors(). Remember: I will treat the Mono.error on the router. If I forgot it the usecase will fail*/
    @Override
    public Mono<AlbumDTO> applyUpdateAlbum(String albumId,AlbumDTO albumDTO) {
        //Refactor
        /*return !Objects.isNull(albumDTO) ? //A static method in final class Objects that allows to check if albumDTO is null
                        !AlbumDTO.thereIsNullAttributes().test(albumDTO) ? //This is a predicate, I create it on AlbumDTO class
                        this.albumRepository
                                .findById(id)
                                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())))
                                .flatMap(album -> {
                                    albumDTO.setIdAlbum(album.getIdAlbum());
                                    return saveAlbumUseCase.applySaveAlbum(albumDTO);
                                })//.map(album -> albumMapper.convertEntityToDTO().apply(album))
                                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_ACCEPTABLE.toString())))
                        : Mono.error(new Throwable(HttpStatus.NOT_ACCEPTABLE.toString()))
                : Mono.error(new Throwable(HttpStatus.NOT_ACCEPTABLE.toString()));*/

        return getAlbumByIdUseCase
                .apply(albumId)
                .flatMap(albumDTOFound -> {
                    Objects.requireNonNull(albumDTO).setIdAlbum(albumDTOFound.getIdAlbum());
                    return saveAlbumUseCase.applySaveAlbum(albumDTO);
                });

    }
}
