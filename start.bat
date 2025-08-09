@echo off
echo ================================
echo   Real Estate Management System
echo ================================
echo.

echo Checking Node.js installation...
node -v >nul 2>&1
if %errorlevel% neq 0 (
    echo.
    echo ❌ Node.js is not installed or not in PATH!
    echo.
    echo Please install Node.js first:
    echo 1. Go to: https://nodejs.org/
    echo 2. Download and install Node.js LTS version
    echo 3. Restart command prompt
    echo 4. Run this script again
    echo.
    pause
    exit /b 1
)

echo ✅ Node.js is installed
node -v
npm -v
echo.

echo Installing dependencies...
npm install
if %errorlevel% neq 0 (
    echo.
    echo ❌ Failed to install dependencies!
    echo Please check your internet connection and try again.
    pause
    exit /b 1
)

echo.
echo ✅ Dependencies installed successfully!
echo.
echo Starting React development server...
echo The application will open at: http://localhost:3000
echo.
echo Demo accounts:
echo   Admin: admin / admin
echo   User:  user / user
echo.
echo Press Ctrl+C to stop the server
echo.
npm start
