package ec.com.reactive.music.album.mapper;

import ec.com.reactive.music.album.collection.Album;
import ec.com.reactive.music.album.dto.AlbumDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
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
