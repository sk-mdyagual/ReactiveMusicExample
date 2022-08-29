package ec.com.reactive.music.service.impl;

import ec.com.reactive.music.domain.dto.PlaylistDTO;
import ec.com.reactive.music.domain.dto.SongDTO;
import ec.com.reactive.music.domain.entities.Playlist;
import ec.com.reactive.music.repository.IPlaylistRepository;
import ec.com.reactive.music.service.IPlaylistService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class PlaylistServiceImpl implements IPlaylistService {
    @Autowired
    private final IPlaylistRepository iPlaylistRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Override
    public Mono<ResponseEntity<Flux<PlaylistDTO>>> findPlaylists() {
        return Mono.justOrEmpty(new ResponseEntity<>(this.iPlaylistRepository
                        .findAll()
                        .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NO_CONTENT.toString())))
                        .map(this::entityToDTO),HttpStatus.FOUND))
                .onErrorResume(throwable -> Mono.just(new ResponseEntity<>(HttpStatus.NO_CONTENT)));
    }

    @Override
    public Mono<ResponseEntity<PlaylistDTO>> findPlaylistById(String id) {
        return this.iPlaylistRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())))
                .map(this::entityToDTO)
                .map(playlistDTO -> new ResponseEntity<>(playlistDTO,HttpStatus.FOUND))
                .onErrorResume(throwable -> Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }

    @Override
    public Mono<ResponseEntity<PlaylistDTO>> savePlaylist(PlaylistDTO pDto) {

        return this.iPlaylistRepository
                .save(dtoToEntity(pDto))
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.EXPECTATION_FAILED.toString())))
                .map(this::entityToDTO)
                .map(playlistDTO -> new ResponseEntity<>(playlistDTO,HttpStatus.CREATED))
                .onErrorResume(throwable -> Mono.just(new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED)));
    }

    @Override
    public Mono<ResponseEntity<PlaylistDTO>> updatePlaylist(String id, PlaylistDTO pDto) {

        return this.iPlaylistRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())))
                .flatMap(playlist -> { //Why flatMap?
                    pDto.setIdPlaylist(playlist.getIdPlaylist());
                    return this.savePlaylist(pDto);
                })
                .map(albumDTOResponseEntity -> new ResponseEntity<>(albumDTOResponseEntity.getBody(),HttpStatus.ACCEPTED))
                .onErrorResume(throwable -> Mono.just(new ResponseEntity<>(HttpStatus.NOT_MODIFIED)));
    }

    @Override
    public Mono<ResponseEntity<PlaylistDTO>> addSongPlaylist(String idPlaylist, SongDTO sDto) {
        return this.iPlaylistRepository
                .findById(idPlaylist)
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())))
                .map(this::entityToDTO)
                .flatMap(playlistDTO -> {
                    playlistDTO.getSongs().add(sDto);

                    var playlistDuration = playlistDTO.getDuration();
                    var songDuration = sDto.getDuration();
                    var total = playlistDuration.plusHours(songDuration.getHour()).plusMinutes(songDuration.getMinute()).plusSeconds(songDuration.getSecond());

                    return this.savePlaylist(playlistDTO.toBuilder().duration(total).build());})
                .map(playlistDTOResponseEntity -> new ResponseEntity<>(playlistDTOResponseEntity.getBody(),HttpStatus.ACCEPTED))
                .onErrorResume(throwable -> Mono.just(new ResponseEntity<>(HttpStatus.NOT_MODIFIED)));
    }



    @Override
    public Mono<ResponseEntity<PlaylistDTO>> removeSongPlaylist(String idPlaylist, SongDTO sDto) {
        return this.iPlaylistRepository
                .findById(idPlaylist)
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())))
                .map(this::entityToDTO)
                .flatMap(playlistDTO -> {
                    playlistDTO.getSongs().remove(sDto);

                    var playlistDuration = playlistDTO.getDuration();
                    var songDuration = sDto.getDuration();
                    var substract = playlistDuration.minusHours(songDuration.getHour()).minusMinutes(songDuration.getMinute()).minusSeconds(songDuration.getSecond());


                    return this.savePlaylist(playlistDTO.toBuilder().duration(substract).build());})
                .map(playlistDTOResponseEntity -> new ResponseEntity<>(playlistDTOResponseEntity.getBody(),HttpStatus.ACCEPTED))
                .onErrorResume(throwable -> Mono.just(new ResponseEntity<>(HttpStatus.NOT_MODIFIED)));
    }

    @Override
    public Mono<ResponseEntity<String>> deletePlaylist(String id) {
        return this.iPlaylistRepository.findById(id)
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())))
                .flatMap(playlist -> this.iPlaylistRepository.deleteById(id))
                .map(voidMono -> new ResponseEntity<>(id,HttpStatus.ACCEPTED))
                .onErrorResume(throwable -> Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }

    @Override
    public PlaylistDTO entityToDTO(Playlist p) {
        return this.modelMapper.map(p,PlaylistDTO.class);
    }

    @Override
    public Playlist dtoToEntity(PlaylistDTO pDto) {
        return this.modelMapper.map(pDto,Playlist.class);
    }
}

