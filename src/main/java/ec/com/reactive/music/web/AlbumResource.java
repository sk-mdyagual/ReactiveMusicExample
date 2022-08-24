package ec.com.reactive.music.web;

import ec.com.reactive.music.domain.dto.AlbumDTO;
import ec.com.reactive.music.service.impl.AlbumServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class AlbumResource {
    @Autowired
    private AlbumServiceImpl albumService;

    //GET
    @GetMapping("/findAlbum/{id}")
    private Mono<ResponseEntity<AlbumDTO>> getAlbumById(@PathVariable String id){
        return albumService.findAlbumById(id);
    }
}
