package ec.com.reactive.music.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.ArrayList;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDTO {
    private String idAlbum;
    private String name;
    private String artist;
    private Integer yearRelease;

}