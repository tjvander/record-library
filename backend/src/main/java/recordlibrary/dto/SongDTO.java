package recordlibrary.dto;

import jakarta.validation.constraints.NotBlank;
import recordlibrary.entity.Song;

public class SongDTO {

    @NotBlank(message = "Song name cannot be blank")
    private String name;

    public SongDTO(){
    }

    public SongDTO(String name){
        this.name=name;
    }

    public SongDTO (Song song)
    {
        this.name=song.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
