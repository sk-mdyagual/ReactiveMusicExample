package ec.com.reactive.music.domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Document(collection = "Song")
@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm:ss")
public class Song {
    @Id
    private String idSong;
    private String name;
    private String lyricsBy;
    private String producedBy;
    private String arrangedBy;
    private LocalTime timestamp;

}