package ec.com.reactive.music.song.mapper;

import ec.com.reactive.music.song.collections.Song;
import ec.com.reactive.music.song.dto.SongDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@AllArgsConstructor
public class SongMapper {

    private final ModelMapper modelMapper;

    public Function<Song, SongDTO> convertEntityToDTO(){
        return song -> modelMapper.map(song, SongDTO.class);
    }

    public Function<SongDTO,Song> convertDTOToEntity(){
        return songDTO -> modelMapper.map(songDTO, Song.class);
    }
}
