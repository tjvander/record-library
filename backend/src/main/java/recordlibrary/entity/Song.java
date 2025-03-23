package recordlibrary.entity;

import jakarta.persistence.*;

@Entity
@Table( uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name", "record_id"})
        })
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "record_id", nullable = false)
    private Record record;

    public Song() {}

    public Song(String name, Record record) {
        this.name = name;
        this.record = record;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }
}
