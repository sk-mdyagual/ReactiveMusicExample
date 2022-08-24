package ec.com.reactive.music.service.impl;

import ec.com.reactive.music.domain.dto.AlbumDTO;
import ec.com.reactive.music.domain.entities.Album;
import ec.com.reactive.music.repository.IAlbumRepository;
import ec.com.reactive.music.service.IAlbumService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AlbumServiceImpl implements IAlbumService {
    @Autowired
    private IAlbumRepository iAlbumRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public Mono<ResponseEntity<Flux<AlbumDTO>>> findAllAlbums() {
        return null;
    }

    @Override
    public Mono<ResponseEntity<AlbumDTO>> findAlbumById(String id) {
        return this.iAlbumRepository
                .findById(id)
                .map(album -> entityToDTO(album))
                .map(albumDTO -> new ResponseEntity<>(albumDTO, HttpStatus.FOUND)); //Mono<ResponseEntity<AlbumDTO>>
    }

    @Override
    public Mono<ResponseEntity<AlbumDTO>> saveAlbum(AlbumDTO albumDTO) {
        return null;
    }




    @Override
    public Album DTOToEntity(AlbumDTO albumDTO) {
        return this.modelMapper.map(albumDTO, Album.class);
    }

    @Override
    public AlbumDTO entityToDTO(Album album) {
        return this.modelMapper.map(album,AlbumDTO.class);
    }
}
