package ec.com.reactive.music.domain.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Document(collection = "Album")
public class Album {
    private String idAlbum = UUID.randomUUID().toString().substring(0, 10);
    private String name;
    private String artist;
    private Integer yearRelease;
    //private ArrayList<Song> songs=new ArrayList<>();

}
