package ec.com.reactive.music.album.usecases;

import ec.com.reactive.music.album.dto.AlbumDTO;
import ec.com.reactive.music.album.mapper.AlbumMapper;
import ec.com.reactive.music.album.repository.IAlbumRepository;
import ec.com.reactive.music.album.usecases.interfaces.UpdateAlbum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;


/*For update use case i create my own interface but it is possible to use a Function too as in GetAlbumByIdUseCase
* because you will send something and return another something*/
@Service
public class UpdateAlbumUseCase implements UpdateAlbum {

    @Autowired
    private IAlbumRepository albumRepository;

    @Autowired
    private AlbumMapper albumMapper;

    /*I'm considering again the scenarios when we sent an empty AlbumDTO object or some of the attributes is null
     * Using the ternary operator I split both cases. On the router, I will manage the catch of the exception dropped by
     * switchIfEmpty also the Mono.errors(). Remember: I will treat the Mono.error on the router. If I forgot it the usecase will fail*/
    @Override
    public Mono<AlbumDTO> applyUpdateAlbum(String id, AlbumDTO albumDTO) {
        return !Objects.isNull(albumDTO)? //A static method in final class Objects that allows to check if albumDTO is null
                        !AlbumDTO.thereIsNullAttributes().test(albumDTO) ? //This is a predicate, I create it on AlbumDTO class
                        this.albumRepository
                                .findById(id)
                                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())))
                                .flatMap(album -> {
                                    album.setIdAlbum(albumDTO.getIdAlbum());
                                    return albumRepository.save(album).map(album1 -> albumMapper.convertEntityToDTO().apply(album1));
                                }).switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_ACCEPTABLE.toString())))
                        : Mono.error(new Throwable(HttpStatus.NOT_ACCEPTABLE.toString()))
                : Mono.error(new Throwable(HttpStatus.NOT_ACCEPTABLE.toString()));

    }
}
