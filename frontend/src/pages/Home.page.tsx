
import { RecordLibrary } from '@/components/recordLibrary/RecordLibrary';
import { ModalsProvider } from '@mantine/modals';


export function HomePage() {
  return (
    <>
    <ModalsProvider labels={{ confirm: 'Submit', cancel: 'Cancel' }}>
      <RecordLibrary />
      </ModalsProvider>
    </>
  );
}
