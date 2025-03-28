# Use the latest Node.js image
FROM node:latest

# Set the working directory
WORKDIR /app

# Copy the package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the Vite project
COPY . .

# Expose the port Vite is using (default is 5173)
EXPOSE 5173

# Start the Vite development server
CMD ["npm", "run", "dev"]
