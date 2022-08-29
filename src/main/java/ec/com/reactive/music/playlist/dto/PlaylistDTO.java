package ec.com.reactive.music.playlist.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ec.com.reactive.music.song.dto.SongDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm:ss")
public class PlaylistDTO {
    private String idPlaylist;
    private String name;
    private String username;
    private ArrayList<SongDTO> songs;
    private LocalTime duration;
}

