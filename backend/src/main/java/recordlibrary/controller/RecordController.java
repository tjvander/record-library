package recordlibrary.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import recordlibrary.dto.RecordDTO;
import recordlibrary.service.RecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/records")
public class RecordController {

    private final RecordService recordService;

    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping
    public List<RecordDTO> getAllRecords() {
        return recordService.getAllRecords();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecordDTO> getRecordById(@PathVariable Long id) {
        return recordService.getRecordById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RecordDTO> createRecord(@Valid @RequestBody RecordDTO record) {
        return ResponseEntity.ok(recordService.createRecord(record));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecordDTO> updateRecord(@PathVariable Long id, @Valid @RequestBody RecordDTO record) {
        try {
            return ResponseEntity.ok(recordService.updateRecord(id, record));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }catch (RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        recordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }
}
