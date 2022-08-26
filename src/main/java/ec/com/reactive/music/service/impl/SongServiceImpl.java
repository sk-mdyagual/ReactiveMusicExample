package ec.com.reactive.music.service.impl;

import ec.com.reactive.music.domain.dto.SongDTO;
import ec.com.reactive.music.domain.entities.Song;
import ec.com.reactive.music.repository.ISongRepository;
import ec.com.reactive.music.service.ISongService;
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
public class SongServiceImpl implements ISongService {
    @Autowired
    private final ISongRepository iSongRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Override
    public Mono<ResponseEntity<Flux<SongDTO>>> findAllSongs() {
        return this.iSongRepository
                .findAll()
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NO_CONTENT.toString())))
                .map(this::entityToDTO)
                .collectList()
                .map(albumDTOS ->  new ResponseEntity<>(Flux.fromIterable(albumDTOS),HttpStatus.FOUND))
                .onErrorResume(throwable -> Mono.just(new ResponseEntity<>(Flux.empty(),HttpStatus.NO_CONTENT)));
    }

    @Override
    public Mono<ResponseEntity<SongDTO>> findSongById(String id) {
        return this.iSongRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())))
                .map(this::entityToDTO)
                .map(songDTO -> new ResponseEntity<>(songDTO,HttpStatus.FOUND))
                .onErrorResume(throwable -> Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }

    @Override
    public Mono<ResponseEntity<SongDTO>> saveSong(SongDTO s) {
        return s.getIdAlbum().equals("Does not exist") ? Mono.just(new ResponseEntity<>(s, HttpStatus.NOT_ACCEPTABLE))
                : this.iSongRepository
                .save(dtoToEntity(s))
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.EXPECTATION_FAILED.toString())))
                .map(this::entityToDTO)
                .map(songDTO -> new ResponseEntity<>(songDTO, HttpStatus.CREATED))
                .onErrorResume(throwable -> Mono.just(new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED)));
    }

    @Override
    public Mono<ResponseEntity<SongDTO>> updateSong(String idSong, SongDTO sDto) {
        return this.iSongRepository
                .findById(idSong)
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())))
                .flatMap(song -> {
                    sDto.setIdSong(song.getIdSong());
                    return this.saveSong(sDto);})
                .map(songDTOResponseEntity -> new ResponseEntity<>(songDTOResponseEntity.getBody(),HttpStatus.ACCEPTED))
                .onErrorResume(throwable -> Mono.just(new ResponseEntity<>(HttpStatus.NOT_MODIFIED)));
    }

    @Override
    public Mono<ResponseEntity<String>> deleteSong(String idSong) {
        return this.iSongRepository
                .findById(idSong)
                .switchIfEmpty(Mono.error(new Throwable(HttpStatus.NOT_FOUND.toString())))
                .flatMap(album -> this.iSongRepository
                        .deleteById(album.getIdAlbum())
                        .map(monoVoid -> new ResponseEntity<>(idSong, HttpStatus.ACCEPTED)))
                .thenReturn(new ResponseEntity<>(idSong, HttpStatus.ACCEPTED))
                .onErrorResume(throwable -> Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }

    @Override
    public SongDTO entityToDTO(Song s) {
        return this.modelMapper.map(s,SongDTO.class);
    }

    @Override
    public Song dtoToEntity(SongDTO sDto) {
        return this.modelMapper.map(sDto,Song.class);
    }
}
