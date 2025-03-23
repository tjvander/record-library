package recordlibrary.service;

import jakarta.persistence.EntityNotFoundException;
import recordlibrary.dto.RecordDTO;
import recordlibrary.dto.SongDTO;
import recordlibrary.entity.Record;
import recordlibrary.entity.Song;
import recordlibrary.repository.RecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecordService {

    private final RecordRepository recordRepository;

    public RecordService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public List<RecordDTO> getAllRecords() {
        return recordRepository.findAll().stream().map(record ->
                new RecordDTO(record.getName(),
                        record.getSongs().stream().map(song -> new SongDTO(song)).collect(Collectors.toList()),
                        record.getId()
                )
        ).collect(Collectors.toList());
    }

    public Optional<RecordDTO> getRecordById(Long id) {
        return recordRepository.findById(id).map(record ->
                new RecordDTO(record.getName(),
                        record.getSongs().stream().map(song -> new SongDTO(song)).collect(Collectors.toList()),
                        record.getId()
                )
        );
    }

    @Transactional
    public RecordDTO createRecord(RecordDTO recordDTO) {
        final Record recordToSave = new Record();
        recordToSave.setName(recordDTO.getName());

        List<Song> songs = recordDTO.getSongs().stream()
                .map(songDTO -> new Song(songDTO.getName(), recordToSave))
                .collect(Collectors.toList());

        recordToSave.setSongs(songs);
        Record record = recordRepository.save(recordToSave);

        return new RecordDTO(record.getName(),
                record.getSongs().stream().map(song -> new SongDTO(song)).collect(Collectors.toList()), record.getId());
    }

    @Transactional
    public RecordDTO updateRecord(Long id, RecordDTO recordDTO) {
        return recordRepository.findById(id).map(record -> {
            record.setName(recordDTO.getName());

            Map<String, Song> existingSongs = record.getSongs().stream()
                    .collect(Collectors.toMap(Song::getName, song -> song));

            List<Song> updatedSongs = new ArrayList<>();

            for (SongDTO songDTO : recordDTO.getSongs()) {
                String songName=songDTO.getName();
                if (existingSongs.containsKey(songName)) {
                    updatedSongs.add(existingSongs.get(songName));
                } else {
                    updatedSongs.add(new Song(songName, record));
                }
            }

            record.getSongs().clear();
            record.getSongs().addAll(updatedSongs);

            recordRepository.save(record);

            return new RecordDTO(record.getName(),
                    record.getSongs().stream().map(song -> new SongDTO(song)).collect(Collectors.toList()), record.getId());
        }).orElseThrow(() -> new EntityNotFoundException("Record not found"));
    }

    public void deleteRecord(Long id) {
        recordRepository.deleteById(id);
    }
}
