package recordlibrary.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class RecordDTO {

    @NotBlank(message = "Record name cannot be blank")
    private String name;
    private Long id;

    @NotEmpty(message = "Song list cannot be empty")
    private List<@Valid SongDTO> songs;

    public RecordDTO() {}

    public RecordDTO(String name, List<SongDTO> songs) {
        this.name = name;
        this.songs = songs;
    }

    public RecordDTO(String name, List<SongDTO> songs, Long id) {
        this.name = name;
        this.songs = songs;
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SongDTO> getSongs() {
        return songs;
    }

    public void setSongs(List<SongDTO> songs) {
        this.songs = songs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
