import { render, screen, fireEvent } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { RecordDialog } from './RecordDialog';
import { ModalsProvider } from '@mantine/modals';
import { MantineProvider } from '@mantine/core';
import { theme } from '../theme';

describe('Record Dialog', () => {

  it('Displays record to update', () => {

    const recordToUpdate:RecordInterface={"name":"Album2","songs":[{"name":"Song A"}, {"name":"Song B"}]};

    render(
      <MantineProvider theme={theme}>
      <ModalsProvider labels={{ confirm: 'Submit', cancel: 'Cancel' }}>
    <RecordDialog  refresh={async ()=>{}} recordToUpdate={recordToUpdate}/>
      </ModalsProvider>
      </MantineProvider>
  );

  expect(screen.getByDisplayValue("Album2")).toBeInTheDocument();

  expect(screen.queryByText('Song A')).toBeInTheDocument();
  expect(screen.queryByText('Song B')).toBeInTheDocument();


  });


  it('Displays validation error', () => {

    render(
      <MantineProvider theme={theme}>
      <ModalsProvider labels={{ confirm: 'Submit', cancel: 'Cancel' }}>
    <RecordDialog  refresh={async ()=>{}} />
      </ModalsProvider>
      </MantineProvider>
  );

  const createButton = screen.getByText('Create a record');
  fireEvent.click(createButton);

  expect(screen.queryByText('Record name is required')).toBeInTheDocument();
  

  });

});
