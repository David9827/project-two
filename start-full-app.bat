@echo off
echo ============================================
echo   Real Estate Management System
echo   Full Stack Application Launcher
echo ============================================
echo.

echo This script will start both backend and frontend
echo.
echo Requirements:
echo - Java JDK 17 or higher
echo - Node.js 16 or higher
echo - Maven (or use included wrapper)
echo.

echo Checking Java...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java not found! Please install JDK 17 or higher
    echo Download from: https://www.oracle.com/java/technologies/downloads/
    echo.
    pause
    exit /b 1
)

echo ✅ Java is available
echo.

echo Checking Node.js...
node -v >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Node.js not found! Please install Node.js 16 or higher
    echo Download from: https://nodejs.org/
    echo.
    pause
    exit /b 1
)

echo ✅ Node.js is available
echo.

echo Starting Backend (Spring Boot)...
echo Opening new window for backend...
start "Backend Server" cmd /c setup-backend.bat

echo.
echo Waiting 10 seconds for backend to start...
timeout /t 10 /nobreak >nul

echo.
echo Starting Frontend (React)...
echo Opening new window for frontend...
start "Frontend Server" cmd /c start.bat

echo.
echo ✅ Both servers are starting!
echo.
echo Backend: http://localhost:8080
echo Frontend: http://localhost:3000
echo.
echo Check the opened windows for server status.
echo Close those windows to stop the servers.
echo.
pause
