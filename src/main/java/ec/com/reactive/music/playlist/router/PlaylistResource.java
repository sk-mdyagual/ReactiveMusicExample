package ec.com.reactive.music.playlist.router;

import org.springframework.web.bind.annotation.*;

@RestController
public class PlaylistResource {
    /*@Autowired
    private IPlaylistService playlistService;

    @Autowired
    private ISongService songService;

    //GET
    @GetMapping("/findPlaylists")
    private Mono<ResponseEntity<Flux<PlaylistDTO>>> getPlaylists(){
        return playlistService.findPlaylists();
    }

    //GET
    @GetMapping("/findPlaylist/{id}")
    private Mono<ResponseEntity<PlaylistDTO>> getPlaylistById(@PathVariable String id){
        return playlistService.findPlaylistById(id);
    }

    //POST
    @PostMapping("/savePlaylist")
    private Mono<ResponseEntity<PlaylistDTO>> postPlaylist(@RequestBody PlaylistDTO pDto){
        return playlistService.savePlaylist(pDto);
    }

    //PUT
    @PutMapping("/updatePlaylist/{id}")
    private Mono<ResponseEntity<PlaylistDTO>> putPlaylist(@PathVariable String id , @RequestBody PlaylistDTO pDto){
        return playlistService.updatePlaylist(id,pDto);
    }

    //PUT
    @PutMapping("/addPlaylistSong/{idPlaylist}/{idSong}")
    private Mono<ResponseEntity<PlaylistDTO>> addSongToPlaylist(@PathVariable String idPlaylist, @PathVariable String idSong){
        return songService.findSongById(idSong) //Mono<ResponseEntity<SongDTO>>
                .flatMap(songDTOResponseEntity -> playlistService.addSongPlaylist(idPlaylist,songDTOResponseEntity.getBody()));


    }

    //PUT
    @PutMapping("/removePlaylistSong/{idPlaylist}/{idSong}")
    private Mono<ResponseEntity<PlaylistDTO>> removeSongToPlaylist(@PathVariable String idPlaylist, @PathVariable String idSong){
        return songService.findSongById(idSong)
                .flatMap(songDTOResponseEntity -> playlistService.removeSongPlaylist(idPlaylist,songDTOResponseEntity.getBody()));


    }

    //DELETE
    @DeleteMapping("/deletePlaylist/{id}")
    private Mono<ResponseEntity<String>> deletePlaylist(@PathVariable String id){
        return playlistService.deletePlaylist(id);
    }*/

}
