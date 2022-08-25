package ec.com.reactive.music.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.ArrayList;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@Getter
//Setter
//@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm:ss")
public class AlbumDTO {
    private String idAlbum ;
    private String name;
    private String artist;
    private Integer yearRelease;
    //private ArrayList<SongDTO> songs=new ArrayList<>();

}