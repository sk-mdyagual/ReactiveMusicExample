package ec.com.reactive.music.web;

import ec.com.reactive.music.domain.dto.AlbumDTO;
import ec.com.reactive.music.service.impl.AlbumServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class AlbumResource {
    @Autowired
    private AlbumServiceImpl albumService;

    @GetMapping("/findAllAlbums")
    private Mono<ResponseEntity<Flux<AlbumDTO>>> getAlbums(){
        return albumService.findAllAlbums();
    }

    //GET
    @GetMapping("/findAlbum/{id}")
    private Mono<ResponseEntity<AlbumDTO>> getAlbumById(@PathVariable String id){
        return albumService.findAlbumById(id);
    }

    //POST
    @PostMapping("/saveAlbum")
    private Mono<ResponseEntity<AlbumDTO>> postAlbum(@RequestBody AlbumDTO aDto){
        return albumService.saveAlbum(aDto);
    }

    //PUT
    @PutMapping("/updateAlbum/{id}")
    private Mono<ResponseEntity<AlbumDTO>> putAlbum(@PathVariable String id , @RequestBody AlbumDTO aDto){
        return albumService.updateAlbum(id,aDto);
    }

    //DELETE
    @DeleteMapping("/deleteAlbum/{id}")
    private Mono<ResponseEntity<String>> deleteAlbum(@PathVariable String id){
        return albumService.deleteAlbum(id);
    }





}
