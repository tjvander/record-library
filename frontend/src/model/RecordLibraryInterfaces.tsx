interface SongInterface {
  name: string;
}

interface RecordInterface {
    name: string;
    id?: number;
    songs: SongInterface[]
  }


  