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

    public static LocalTime addDuration(LocalTime playlistDuration, LocalTime songDuration){
        return playlistDuration.plusHours(songDuration.getHour()).plusMinutes(songDuration.getMinute()).plusSeconds(songDuration.getSecond());
    }

    public static LocalTime removeDuration(LocalTime playlistDuration, LocalTime songDuration){
        return playlistDuration.minusHours(songDuration.getHour()).minusMinutes(songDuration.getMinute()).minusSeconds(songDuration.getSecond());
    }
}

