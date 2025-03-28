import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import path from 'path';

// Export a single default config object
export default defineConfig({
  server: {
    host: '0.0.0.0',  // This makes the server accessible externally
    port: 5173,        // Ensure you're using the correct port
  },
  plugins: [react()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src'), // This tells Vite that '@' maps to 'src' directory
    },
  },
});
