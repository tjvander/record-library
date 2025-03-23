package recordlibrary.service;

import recordlibrary.dto.RecordDTO;
import recordlibrary.dto.SongDTO;
import recordlibrary.entity.Record;
import recordlibrary.entity.Song;
import recordlibrary.repository.RecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecordServiceTest {

    @Mock
    private RecordRepository recordRepository;

    @InjectMocks
    private RecordService recordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRecord() {
        Record record = new Record("Album 1", List.of(new Song("Song 2", null)));
        when(recordRepository.save(any())).thenReturn(record);

        RecordDTO recordDTO = new RecordDTO("Album 1", List.of(new SongDTO("Song 2")));
        RecordDTO savedRecord = recordService.createRecord(recordDTO);

        assertEquals("Album 1", savedRecord.getName());
        assertEquals(1, savedRecord.getSongs().size());
        assertEquals("Song 2", savedRecord.getSongs().get(0).getName());
    }
}
