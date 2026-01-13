# CorpPark 360 - Quick Start Scripts

## Windows PowerShell Scripts

### Start Backend Server
```powershell
# Navigate to backend and start Spring Boot
cd backend
mvn clean spring-boot:run
```

### Start Frontend Development Server
```powershell
# Navigate to frontend and start Angular
cd frontend
npm install
npm start
```

### Start Both (Run in separate terminals)

**Terminal 1 - Backend:**
```powershell
cd C:\Users\vishn\IdeaProjects\Parking-Management-System\corppark-360\backend
mvn spring-boot:run
```

**Terminal 2 - Frontend:**
```powershell
cd C:\Users\vishn\IdeaProjects\Parking-Management-System\corppark-360\frontend
npm start
```

## One-Line Commands

### Backend
```powershell
cd backend; mvn spring-boot:run
```

### Frontend
```powershell
cd frontend; npm install; npm start
```

## Database Setup

### MySQL Commands
```powershell
# Connect to MySQL
mysql -u root -p

# In MySQL prompt:
CREATE DATABASE IF NOT EXISTS corppark360;
USE corppark360;
SOURCE db/schema.sql;
SOURCE db/seed.sql;
exit;
```

### Verify Database
```powershell
mysql -u root -p -e "USE corppark360; SHOW TABLES;"
```

## Build for Production

### Backend JAR
```powershell
cd backend
mvn clean package
java -jar target/corppark360-backend.jar
```

### Frontend Production Build
```powershell
cd frontend
npm run build
# Output will be in frontend/dist/
```

## Environment Variables (Optional)

### Backend Configuration
```powershell
# Set environment variables before running
$env:DB_URL="jdbc:mysql://localhost:3306/corppark360"
$env:DB_USER="root"
$env:DB_PASSWORD="your_password"
$env:PORT="8080"

# Then run
cd backend; mvn spring-boot:run
```

### Frontend API URL
Edit `frontend/src/app/services/api.service.ts`:
```typescript
private baseUrl = 'http://localhost:8080/api';
```

## Testing URLs

After starting both servers:

- **Frontend**: http://localhost:4200
- **Backend API**: http://localhost:8080/api
- **Health Check**: http://localhost:8080/api/floors

## Quick Test Flow

1. Start MySQL (ensure it's running)
2. Open Terminal 1: Start Backend
3. Open Terminal 2: Start Frontend
4. Open Browser: http://localhost:4200
5. Navigate to **Live Lot** to see the parking UI

## Troubleshooting

### Port Already in Use

**Backend (8080):**
```powershell
# Find process using port 8080
netstat -ano | findstr :8080
# Kill process (replace PID with actual process ID)
taskkill /PID <PID> /F
```

**Frontend (4200):**
```powershell
# Find process using port 4200
netstat -ano | findstr :4200
# Kill process
taskkill /PID <PID> /F
```

### Node Modules Issues
```powershell
cd frontend
Remove-Item -Recurse -Force node_modules
Remove-Item package-lock.json
npm install
```

### Maven Cache Issues
```powershell
cd backend
mvn clean
mvn dependency:purge-local-repository
mvn install
```

### Database Connection Issues
1. Check MySQL is running: `Get-Service MySQL*`
2. Verify credentials in `backend/src/main/resources/application.yml`
3. Test connection: `mysql -u root -p -e "SELECT 1;"`

## Development Tips

### Hot Reload
- **Frontend**: Automatically reloads on file changes
- **Backend**: Use Spring DevTools for hot reload (already configured)

### View Logs
- **Backend**: Console output in Terminal 1
- **Frontend**: Console output in Terminal 2 + Browser DevTools

### API Testing
Use curl or Postman:
```powershell
# Test entry endpoint
curl -Method POST -Uri "http://localhost:8080/api/entry" `
  -Headers @{"Content-Type"="application/json"} `
  -Body '{"empId":"E001"}'

# Get floors
curl -Uri "http://localhost:8080/api/floors"

# Get slots for floor 2
curl -Uri "http://localhost:8080/api/slots?floorNo=2"
```

## IDE Configuration

### IntelliJ IDEA
1. Open project root folder
2. Import as Maven project (backend)
3. Enable Angular Language Service
4. Run configurations:
   - Backend: Maven → spring-boot:run
   - Frontend: npm → start

### VS Code
1. Install extensions:
   - Spring Boot Extension Pack
   - Angular Language Service
2. Open integrated terminal
3. Run commands as shown above

---

**Quick Reference Card:**
```
Backend Start:  cd backend && mvn spring-boot:run
Frontend Start: cd frontend && npm start
View App:       http://localhost:4200/lot
API Docs:       See Documentation.md
UI Guide:       See PARKING_LOT_UI_GUIDE.md
```

