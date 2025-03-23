package recordlibrary.controller;

import recordlibrary.dto.RecordDTO;
import recordlibrary.dto.SongDTO;
import recordlibrary.entity.Record;
import recordlibrary.entity.Song;
import recordlibrary.repository.RecordRepository;
import recordlibrary.repository.SongRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RecordControllerITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        songRepository.deleteAll();
        recordRepository.deleteAll();
    }

    @Test
    void testCreateRecord() throws Exception {
        RecordDTO recordDTO = new RecordDTO("New Album", List.of(new SongDTO("Song X"),new SongDTO("Song Y")));

        mockMvc.perform(post("/records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recordDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Album"))
                .andExpect(jsonPath("$.songs", hasSize(2)));
    }

    @Test
    void testGetAllRecords() throws Exception {
        Record record = new Record("Album 1");
        record.setSongs(List.of(new Song("Song A", record),new Song("Song B", record)));
        record = recordRepository.save(record);

        mockMvc.perform(get("/records"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Album 1"))
                .andExpect(jsonPath("$[0].songs", hasSize(2)));
    }

    @Test
    void testGetRecordById_Found() throws Exception {
        Record record = new Record("Album 1");
        record.setSongs(List.of(new Song("Song A", record),new Song("Song B", record)));
        record = recordRepository.save(record);

        mockMvc.perform(get("/records/" + record.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Album 1"))
                .andExpect(jsonPath("$.songs", hasSize(2)));
    }

    @Test
    void testGetRecordById_NotFound() throws Exception {
        mockMvc.perform(get("/records/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateRecord_Success() throws Exception {
        Record record = new Record("Old Album");
        record.setSongs(List.of(new Song("Old Song 1", record),new Song("Old Song 2", record)));
        record = recordRepository.save(record);

        RecordDTO updatedRecord = new RecordDTO("Updated Album", List.of(new SongDTO("New Song 1"), new SongDTO("New Song 2")));

         mockMvc.perform(put("/records/" + record.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRecord)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Album"))
                .andExpect(jsonPath("$.songs", hasSize(2)));
    }

    @Test
    void testUpdateRecord_NotFound() throws Exception {
        RecordDTO updatedRecord = new RecordDTO("Updated Album", List.of(new SongDTO("Song X")));

        mockMvc.perform(put("/records/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedRecord)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteRecord() throws Exception {
        Record record = new Record("Album to Delete");
        record = recordRepository.save(record);

        mockMvc.perform(delete("/records/" + record.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/records/" + record.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateRecordWithInvalidSongData() throws Exception {
        RecordDTO recordDTO = new RecordDTO("New Album",new ArrayList<>());

        mockMvc.perform(post("/records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recordDTO)))
                .andExpect(status().isBadRequest());

        recordDTO = new RecordDTO("New Album 2",List.of(new SongDTO(""), new SongDTO("New Song 2")));

        mockMvc.perform(post("/records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recordDTO)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void testCreateRecordWithInvalidRecordData() throws Exception {
        RecordDTO recordDTO = new RecordDTO("", List.of(new SongDTO("Song X")));

        mockMvc.perform(post("/records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recordDTO)))
                .andExpect(status().isBadRequest());

    }
}
