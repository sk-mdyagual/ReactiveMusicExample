package ec.com.reactive.music.playlist.mapper;

import ec.com.reactive.music.album.collection.Album;
import ec.com.reactive.music.album.dto.AlbumDTO;
import ec.com.reactive.music.playlist.collection.Playlist;
import ec.com.reactive.music.playlist.dto.PlaylistDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.function.Function;


/*
* This class-level annotation treats it as a custom bean.
* In other words, without having to write any explicit code, Spring will:
* 1. Scan our application for classes annotated with @Component.
* 2. Instantiate them and inject any specified dependencies into them.
* */
@Component
@AllArgsConstructor
public class PlaylistMapper {

    private final ModelMapper modelMapper;

    public Function<Playlist, PlaylistDTO> convertEntityToDTO(){
        return playlist ->
                modelMapper.map(playlist, PlaylistDTO.class);
    }

    public  Function<PlaylistDTO, Playlist> convertDTOToEntity (){
        return playlistDTO ->
                modelMapper.map(playlistDTO,Playlist.class);
    }
}
