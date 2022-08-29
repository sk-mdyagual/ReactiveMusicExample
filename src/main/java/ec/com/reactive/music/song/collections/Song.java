package ec.com.reactive.music.song.collections;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Song")
@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm:ss")
public class Song {
    @Id
    private String idSong = UUID.randomUUID().toString().substring(0, 10);
    private String name;
    private String idAlbum;
    private String lyricsBy;
    private String producedBy;
    private String arrangedBy;
    private LocalTime duration;

}