package ec.com.reactive.music.album.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ec.com.reactive.music.album.collection.Album;
import lombok.*;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AlbumDTO {
    private String idAlbum;
    private String name;
    private String artist;
    private Integer yearRelease;

    /*This predicate was created to verify if an attribute is not sent throught the postman's body, equally to said it is null
    * I made use of final class Optional and its static method .ofNullable() and .isEmpty() to ensure all */
    public static Predicate<AlbumDTO> thereIsNullAttributes(){
        return albumDTO -> Optional.ofNullable(albumDTO.getName()).isEmpty()
                || Optional.ofNullable(albumDTO.getArtist()).isEmpty()
                || Optional.ofNullable(albumDTO.getYearRelease()).isEmpty();
    }

}