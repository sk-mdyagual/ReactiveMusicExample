package ec.com.reactive.music.album.usecases;

import ec.com.reactive.music.album.dto.AlbumDTO;
import ec.com.reactive.music.album.mapper.AlbumMapper;
import ec.com.reactive.music.album.repository.IAlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;
/*
A Supplier interface is used to return an object or objects requested without sending any parameters because it is not needed
So, in this use case we will call all the possible registers located in Album collection.
Do you need parameters to acomplish that? Nope.
So, Supplier is useful here.
* */
@Service
public class GetAlbumsUseCase implements Supplier<Flux<AlbumDTO>> {

    @Autowired
    private IAlbumRepository albumRepository;

    @Autowired
    private AlbumMapper albumMapper;
    /*When u implement Supplier you always have to override the get() method, then you will call in on the router for the
    * consumption.  In this get() also I'm treating the case where there is nothing in the collection. The management of the Httpstatus it is done
    * on the router
    *
    * */
    @Override
    public Flux<AlbumDTO> get() {
        return albumRepository
                .findAll()
                .switchIfEmpty(Flux.empty()) //If there is nothing, switch to nothing dude and go to onErrorResume
                .map(album -> albumMapper.convertEntityToDTO().apply(album)) //Otherwise return the Flux of DTO's albums
                .onErrorResume(throwable -> Flux.empty()); //Return nothing when drops on switch
    }
}
