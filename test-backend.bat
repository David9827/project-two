@echo off
echo ============================================
echo   Testing Real Estate Backend with Database
echo ============================================
echo.

echo Step 1: Checking if MySQL is running...
echo Please make sure:
echo 1. MySQL Server is running on port 3306
echo 2. Database 'estatebasic' exists
echo 3. Tables 'building' and 'district' exist
echo.

echo Step 2: Testing database connection...
echo This will try to connect to: jdbc:mysql://localhost:3306/estatebasic
echo Username: root
echo Password: Dovancong2004@
echo.

echo Step 3: Starting Spring Boot backend...
echo.
echo Backend will be available at: http://localhost:8080
echo Test endpoints:
echo   - Health check: http://localhost:8080/api/health
echo   - Get all buildings: http://localhost:8080/api/building
echo   - Search buildings: http://localhost:8080/api/building/search (POST)
echo.
echo If database connection fails, the app will use mock data.
echo.
echo Press any key to start backend...
pause

echo.
echo Starting backend...
mvn spring-boot:run





