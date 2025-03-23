import { Button, Group, List, Stack, Text, Title } from '@mantine/core';
import classes from './RecordLibrary.module.css';
import { useEffect, useState } from 'react';
import { modals } from '@mantine/modals';
import { RecordDialog } from '../RecordDialog';

export function RecordLibrary() {

  const [records, setRecords] = useState<RecordInterface[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const fetchRecords = async () => {
      try {
        setLoading(true);
        const response = await fetch('http://localhost:8080/records');
        
        if (!response.ok) {
          throw new Error(`Error: ${response.status}`);
        }
        
        const data = await response.json();
        setRecords(data);
        setError(null);
      } catch (err: unknown) {
        if (err instanceof Error) {
          setError(`Failed to fetch records: ${err.message}`);
          console.error(err.message);
        } else {
          setError('An unknown error occurred');
          console.error('Unknown error:', err);
        }
      } finally {
        setLoading(false);
      }
    };
  
    const deleteRecord = async (recordId: number) => {
      try {
        setLoading(true);
        const response = await fetch(`http://localhost:8080/records/${recordId}`, {
          method: 'DELETE',
        });
        
        if (!response.ok) {
          throw new Error(`Error: ${response.status}`);
        }
        
        await fetchRecords();
      } catch (err: unknown) {
        if (err instanceof Error) {
          setError(`Failed to delete record: ${err.message}`);
          console.error(err.message);
        } else {
          setError('An unknown error occurred while deleting');
          console.error('Unknown error:', err);
        }
        setLoading(false); 
      }
    };

    useEffect(() => {
      fetchRecords();
    }, []);
  
    const updateRecord =(value:RecordInterface) =>{
      modals.open({
        title: 'Update a record',
        children: (
        <RecordDialog
          refresh={fetchRecords}
          recordToUpdate={value}
        />
        )
      });
  
    }

  const addRecord =() =>{
    modals.open({
      title: 'Add a record',
      children: (
      <RecordDialog
        refresh={fetchRecords}
      />
      )
    });

  }

  return (
    <>
      <Title className={classes.title} ta="center" mt={100}>
        <Text inherit variant="gradient" component="span" gradient={{ from: 'pink', to: 'yellow' }}>
          Records
        </Text>
      </Title>
    
      <Stack align='center'>
      <List>
      {records.map((item)=>(
          <List.Item key={'record'+item.id} className={classes.item}>
            <Group>
            <Text  onClick={()=>updateRecord(item)}>{item.name} </Text>  
            <Button size="compact-xs" disabled={!item.id} onClick={() => deleteRecord(item.id||-1)}>Delete</Button>
            </Group>
          </List.Item>
          )
        )
        }
        </List>
        <Group>
        <Button size="Add a Record" onClick={() => addRecord()}>Add a record</Button>
        </Group>
        </Stack>

    </>
  );
}
