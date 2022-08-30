package ec.com.reactive.music.album.mapper;

import ec.com.reactive.music.album.collection.Album;
import ec.com.reactive.music.album.dto.AlbumDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
/*
* This class-level annotation treats it as a custom bean.
* In other words, without having to write any explicit code, Spring will:
* 1. Scan our application for classes annotated with @Component.
* 2. Instantiate them and inject any specified dependencies into them.
* */
public class AlbumMapper {
    @Autowired
    private ModelMapper modelMapper;


    public Function<Album, AlbumDTO> convertEntityToDTO(){
        return album ->
                modelMapper.map(album, AlbumDTO.class);
    }

    public  Function<AlbumDTO, Album> convertDTOToEntity (){
        return albumDTO ->
                modelMapper.map(albumDTO,Album.class);
    }
}
