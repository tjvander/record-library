import { useState } from "react";
import { Button, TextInput, Group, Stack, List } from "@mantine/core";
import { useForm } from "@mantine/form";
import { modals } from '@mantine/modals';

export interface RecordDialogProps{
  refresh: () => Promise<void>,
  recordToUpdate?: RecordInterface
}

export function RecordDialog({ refresh, recordToUpdate }: RecordDialogProps) {
  const form = useForm<RecordInterface>({
    initialValues: {
      name: recordToUpdate?recordToUpdate.name:"",
      id: recordToUpdate?recordToUpdate.id:-1,
      songs: recordToUpdate?recordToUpdate.songs: [],
    },
    validate: {
      name: (value) => (value.trim().length > 0 ? null : "Record name is required"),
      songs: (value) =>
        value.length === 0
          ? "At least one song is required"
          : value.some((song: SongInterface) => song.name.trim().length === 0)
          ? "All songs must have names"
          : null,
    },
  });

  const saveRecord = async (values:RecordInterface) => {
    try {
      const response = await fetch("http://localhost:8080/records", {
        method: "POST",
        body: JSON.stringify(values),
        headers: {
          "Content-type": "application/json; charset=UTF-8"
        }
      }
    );
    if (!response.ok) {
        throw new Error(`Error: ${response.status}`);
      }   
      const data = await response.json();
    } catch (err: unknown) {
      if (err instanceof Error) {
        console.error(err.message);
      } else {
        console.error('Unknown error:', err);
      }
    } 
  };

  const updateRecord = async (values:RecordInterface) => {
    try {
      const response = await fetch(`http://localhost:8080/records/${values.id}`, {
        method: "PUT",
        body: JSON.stringify(values),
        headers: {
          "Content-type": "application/json; charset=UTF-8"
        }
      }
    );
    if (!response.ok) {
        throw new Error(`Error: ${response.status}`);
      }   
      const data = await response.json();
    } catch (err: unknown) {
      if (err instanceof Error) {
        console.error(err.message);
      } else {
        console.error('Unknown error:', err);
      }
    } 
  };

  const [newSong, setNewSong] = useState("");

  const addSong = () => {
    if (newSong.trim().length > 0) {
      form.setFieldValue("songs", [...form.values.songs, { name: newSong }]);
      setNewSong("");
    }
  };

  const removeSong = (index: number) => {
    form.setFieldValue(
      "songs",
      form.values.songs.filter((_, i) => i !== index)
    );
  };

  const handleSubmit = async (values: RecordInterface) => {
    if(recordToUpdate){
      await updateRecord(values);
    }
    else{
      await saveRecord (values);
    }
    form.reset();
    refresh();
    modals.closeAll()
  };

  return (
    <form onSubmit={form.onSubmit(handleSubmit)}>
      <Stack>
        <TextInput label="Record Name" {...form.getInputProps("name")} />
        <List spacing="xs" size="sm" withPadding>
          {form.values.songs.map((song, index) => (
            <List.Item key={index}>
              <Group>
                {song.name}
                <Button onClick={() => removeSong(index)} size="compact-xs">
                Remove
                </Button>
              </Group>
            </List.Item>
          ))}
        </List>
        {form.errors.songs && <div style={{ color: "red" }}>{form.errors.songs}</div>}
        <Group>
          <TextInput
            placeholder="Enter song name"
            value={newSong}
            onChange={(event) => setNewSong(event.currentTarget.value)}
          />
          <Button onClick={addSong} variant="outline">
            Add Song
          </Button>
        </Group>
        <Button type="submit">{recordToUpdate?'Update record':'Create a record'}</Button>
      </Stack>
    </form>
  );
}