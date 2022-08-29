package ec.com.reactive.music.usecases.interfaces;

import ec.com.reactive.music.album.dto.AlbumDTO;
import ec.com.reactive.music.album.collection.Album;
import ec.com.reactive.music.album.repository.IAlbumRepository;
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
@ExtendWith(MockitoExtension.class)
class AlbumServiceImplTest {

    @Mock
    IAlbumRepository albumRepositoryMock;

    ModelMapper modelMapper; //Helper - Apoyo/Soporte

    /*AlbumServiceImpl albumService;

    @BeforeEach
    void init(){
        modelMapper = new ModelMapper();
        albumService = new AlbumServiceImpl(albumRepositoryMock,modelMapper);
    }

    //The test is focused on the status code
    @Test
    @DisplayName("findAllAlbums()")
    void findAllAlbums() {
        //1. Que tipo de prueba voy a hacer? Exitosa o fallida. - Exitosa -> Resultado: Mono<ResponseEntity<Flux<AlbumDTO>>>

        //2. Armar el escenario con la respuesta esperada
        ArrayList<Album> listAlbums = new ArrayList<>();
        listAlbums.add(new Album());
        listAlbums.add(new Album());

        ArrayList<AlbumDTO> listAlbumsDTO = listAlbums.stream().map(album -> modelMapper.map(album,AlbumDTO.class)).collect(Collectors.toCollection(ArrayList::new));

        var fluxResult = Flux.fromIterable(listAlbums);
        var fluxResultDTO = Flux.fromIterable(listAlbumsDTO);

        //La respuesta esperada
        ResponseEntity<Flux<AlbumDTO>> respEntResult = new ResponseEntity<>(fluxResultDTO, HttpStatus.FOUND);

        //3. Mockeo - Mockear el resultado esperado
        Mockito.when(albumRepositoryMock.findAll()).thenReturn(fluxResult);

        //4. Servicio
        var service = albumService.findAllAlbums();

        //5. Stepverifier
        StepVerifier.create(service)
                .expectNextMatches(fluxResponseEntity -> fluxResponseEntity.getStatusCode().is3xxRedirection())
                .expectComplete().verify();

        //6. Verificación de que se está usando lo que se mockeo en el punto 3
        //Mockito.verify(albumRepositoryMock.findAll());

    }

    @Test
    @DisplayName("findAlbumById()")
    void findAlbumById() {
        Album albumExpected = new Album();
        albumExpected.setIdAlbum("12345678-9");
        albumExpected.setName("albumTesting1");
        albumExpected.setArtist("testerArtist");
        albumExpected.setYearRelease(2015);

        var albumDTOExpected = modelMapper.map(albumExpected,AlbumDTO.class);

        ResponseEntity<AlbumDTO> albumDTOResponse = new ResponseEntity<>(albumDTOExpected,HttpStatus.FOUND);

        Mockito.when(albumRepositoryMock.findById(Mockito.any(String.class))).thenReturn(Mono.just(albumExpected));

        var service = albumService.findAlbumById("12345678-9");

        StepVerifier.create(service)
                .expectNext(albumDTOResponse)
                .expectComplete()
                .verify();

        //Si está utilizando lo que yo mockee
        Mockito.verify(albumRepositoryMock).findById("12345678-9");
    }

    @Test
    @DisplayName("findAlbumByIdError()")
    void findAlbumByIdError() { //Not found

        ResponseEntity<AlbumDTO> albumDTOResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Mockito.when(albumRepositoryMock.findById(Mockito.any(String.class))).thenReturn(Mono.empty());

        var service = albumService.findAlbumById("12345678-9");

        StepVerifier.create(service)
                .expectNext(albumDTOResponse)
                .expectComplete().verify();

        Mockito.verify(albumRepositoryMock).findById("12345678-9");
    }
    @Test
    @DisplayName("saveAlbum()")
    void saveAlbum(){
        Album albumExpected = new Album();
        albumExpected.setIdAlbum("12345678-9");
        albumExpected.setName("albumTesting1");
        albumExpected.setArtist("testerArtist");
        albumExpected.setYearRelease(2015);

        var albumDTOExpected = modelMapper.map(albumExpected,AlbumDTO.class);

        ResponseEntity<AlbumDTO> albumDTOResponse = new ResponseEntity<>(albumDTOExpected,HttpStatus.CREATED);

        Mockito.when(albumRepositoryMock.save(Mockito.any(Album.class))).thenReturn(Mono.just(albumExpected));

        var service = albumService.saveAlbum(albumDTOExpected);

        StepVerifier.create(service)
                .expectNext(albumDTOResponse)
                .expectComplete()
                .verify();

        //Si está utilizando lo que yo mockee
        Mockito.verify(albumRepositoryMock).save(albumExpected);
    }

    @Test
    @DisplayName("updateAlbum()")
    void updateAlbum(){
        Album albumExpected = new Album();
        albumExpected.setIdAlbum("12345678-9");
        albumExpected.setName("albumTesting");
        albumExpected.setArtist("testerArtist");
        albumExpected.setYearRelease(2015);

        var albumEdited = albumExpected.toBuilder().name("albumTestingEdited").build();

        var albumDTOEdited = modelMapper.map(albumEdited,AlbumDTO.class);


        ResponseEntity<AlbumDTO> albumDTOResponse = new ResponseEntity<>(albumDTOEdited,HttpStatus.ACCEPTED);

        //You need to mock the findById first and because you use it previous the save/update
        Mockito.when(albumRepositoryMock.findById(Mockito.any(String.class))).thenReturn(Mono.just(albumExpected));
        Mockito.when(albumRepositoryMock.save(Mockito.any(Album.class))).thenReturn(Mono.just(albumEdited));

        var service = albumService.updateAlbum("12345678-9", albumDTOEdited);

        StepVerifier.create(service)
                .expectNext(albumDTOResponse)
                .expectComplete()
                .verify();

        //Si está utilizando lo que yo mockee
        Mockito.verify(albumRepositoryMock).save(albumEdited);

    }

    @Test
    @DisplayName("deleteAlbum()")
    void deleteAlbum(){
        Album albumExpected = new Album();
        albumExpected.setIdAlbum("12345678-9");
        albumExpected.setName("albumTesting");
        albumExpected.setArtist("testerArtist");
        albumExpected.setYearRelease(2015);

        ResponseEntity<String> responseDelete = new ResponseEntity<>(albumExpected.getIdAlbum(),HttpStatus.ACCEPTED);

        Mockito.when(albumRepositoryMock.findById(Mockito.any(String.class)))
                .thenReturn(Mono.just(albumExpected));
        Mockito.when(albumRepositoryMock.deleteById(Mockito.any(String.class)))
                .thenReturn(Mono.empty());


        var service = albumService.deleteAlbum("12345678-9");


        StepVerifier.create(service).expectNext(responseDelete).expectComplete().verify();

        Mockito.verify(albumRepositoryMock).findById("12345678-9");
        Mockito.verify(albumRepositoryMock).deleteById("12345678-9");

    }*/
}