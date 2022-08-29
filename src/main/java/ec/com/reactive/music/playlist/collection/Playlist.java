package ec.com.reactive.music.playlist.collection;

import com.fasterxml.jackson.annotation.JsonFormat;
import ec.com.reactive.music.song.collections.Song;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Playlist")
@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm:ss")
public class Playlist {
    @Id
    private String idPlaylist = UUID.randomUUID().toString().substring(0, 10);
    private String name;
    private String username;
    private ArrayList<Song> songs;
    private LocalTime duration = LocalTime.of(0,0,0);
}
