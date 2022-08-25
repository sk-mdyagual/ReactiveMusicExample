package ec.com.reactive.music.service.impl;

import ec.com.reactive.music.domain.dto.AlbumDTO;
import ec.com.reactive.music.domain.entities.Album;
import ec.com.reactive.music.repository.IAlbumRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class AlbumServiceImplTest {

    @Mock
    IAlbumRepository albumRepositoryMock;

    ModelMapper modelMapper; //Helper - Apoyo/Soporte

    AlbumServiceImpl albumService;

    @BeforeEach
    void init(){
        modelMapper = new ModelMapper();
        albumService = new AlbumServiceImpl(albumRepositoryMock,modelMapper);
    }

    @Test
    @DisplayName("findAllAlbums()")
    void findAllAlbums() {
        //1. Que tipo de prueba voy a hacer? Exitosa o fallida. - Exitosa -> Resultado: Mono<ResponseEntity<Flux<AlbumDTO>>>

        //2. Armar el escenario con la respuesta esperada
        ArrayList<Album> listAlbums = new ArrayList<>();
        listAlbums.add(new Album());
        listAlbums.add(new Album());

        var fluxResult = Flux.fromIterable(listAlbums);
        var fluxResultDTO = Flux
                .fromIterable(listAlbums.stream().map(album -> modelMapper.map(album,AlbumDTO.class))
                        .collect(Collectors.toCollection(ArrayList::new)));

        //La respuesta esperada
        ResponseEntity<Flux<AlbumDTO>> RespEntResult = new ResponseEntity<>(fluxResultDTO, HttpStatus.FOUND);

        //3. Mockeo - Mockear el resultado esperado
        Mockito.when(albumRepositoryMock.findAll()).thenReturn(fluxResult);

        //4. Servicio
        var service = albumService.findAllAlbums();

        //5. Stepverifier
        StepVerifier.create(service)
                .expectNext(RespEntResult)
                .expectComplete()
                .verify();

        //6. Verificación de que se está usando lo que se mockeo en el punto 3
        Mockito.verify(albumRepositoryMock.findAll());

    }

    @Test
    void findAlbumById() {
        Album albumExpected = new Album();
        albumExpected.setIdAlbum("12345678-9");
        albumExpected.setName("albumTesting1");
        albumExpected.setArtist("testerArtist");
        albumExpected.setYearRelease(2015);

        var albumDTOExpected = modelMapper.map(albumExpected, AlbumDTO.class);

        ResponseEntity<AlbumDTO> albumDTOResponseEntity = new ResponseEntity<>(albumDTOExpected,HttpStatus.FOUND);

        Mockito.when(albumRepositoryMock.findById(Mockito.any(String.class))).thenReturn(Mono.just(albumExpected));

        var service = albumService.findAlbumById("12345678-9");

        StepVerifier.create(service).expectNext(albumDTOResponseEntity).expectComplete().verify();

        //Mockito.verify(albumRepositoryMock.findById("12345678-9"));

    }

    @Test
    void findAlbumByIdError() { //Not found

        ResponseEntity<AlbumDTO> albumDTOResponseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Mockito.when(albumRepositoryMock.findById(Mockito.any(String.class))).thenReturn(Mono.empty());

        var service = albumService.findAlbumById("12345678-9");

        StepVerifier.create(service).expectNext(albumDTOResponseEntity).expectComplete().verify();

        //Mockito.verify(albumRepositoryMock.findById("12345678-9"));

    }
    @Test
    void saveAlbum() {
    }

    @Test
    void updateAlbum() {
    }

    @Test
    void deleteAlbum() {
    }
}