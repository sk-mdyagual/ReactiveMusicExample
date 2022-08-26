package ec.com.reactive.music.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@Getter
//@Setter
@Builder(toBuilder = true)
@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm:ss")
public class SongDTO {
    private String idSong;
    private String name;
    private String idAlbum;
    private String lyricsBy;
    private String producedBy;
    private String arrangedBy;
    private LocalTime duration;

}
