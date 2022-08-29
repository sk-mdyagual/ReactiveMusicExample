package ec.com.reactive.music.web;

import ec.com.reactive.music.domain.dto.SongDTO;
import ec.com.reactive.music.service.IAlbumService;
import ec.com.reactive.music.service.ISongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class SongResource {
    @Autowired
    private ISongService songService;

    @Autowired
    private IAlbumService albumService;

    //GET
    @GetMapping("/findAllSongs")
    private Mono<ResponseEntity<Flux<SongDTO>>> getSongs(){
        return songService.findAllSongs();
    }

    //GET
    @GetMapping("/findSong/{id}")
    private Mono<ResponseEntity<SongDTO>> getSongById(@PathVariable String id){
        return songService.findSongById(id);
    }

    //POST
    @PostMapping("/saveSong")
    private Mono<ResponseEntity<SongDTO>> postSong(@RequestBody SongDTO sDto){
        return albumService.findAlbumById(sDto.getIdAlbum())
                .flatMap(albumDTOResponseEntity -> albumDTOResponseEntity.getStatusCode().is4xxClientError() ?
                        songService.saveSong(sDto.toBuilder().idAlbum("Does not exist").build())
                        : songService.saveSong(sDto));
    }

    //PUT
    @PutMapping("/updateSong/{id}")
    private Mono<ResponseEntity<SongDTO>> putSong(@PathVariable String id, @RequestBody SongDTO sDto){
        return songService.updateSong(id,sDto);
    }

    //DELETE
    @DeleteMapping("/deleteSong/{id}")
    private Mono<ResponseEntity<String>> deleteSong(@PathVariable String id){
        return songService.deleteSong(id);
    }
}
