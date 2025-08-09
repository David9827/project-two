@echo off
echo ========================================
echo   Quick Fix and Start - Real Estate App
echo ========================================
echo.

echo Step 1: Checking if Node.js is installed...
node -v >nul 2>&1
if %errorlevel% neq 0 (
    echo.
    echo ❌ ERROR: Node.js is not installed!
    echo.
    echo To fix this:
    echo 1. Open browser and go to: https://nodejs.org/
    echo 2. Download Node.js LTS version (recommend 18.x or 20.x)
    echo 3. Install it (just click Next, Next, Install)
    echo 4. Restart your computer or command prompt
    echo 5. Run this script again
    echo.
    echo Opening Node.js download page...
    start https://nodejs.org/
    echo.
    pause
    exit /b 1
)

echo ✅ Node.js found!
echo Version: 
node -v
npm -v
echo.

echo Step 2: Installing React dependencies...
echo This may take a few minutes on first run...
npm install
if %errorlevel% neq 0 (
    echo.
    echo ❌ Failed to install dependencies!
    echo Try these solutions:
    echo 1. Check internet connection
    echo 2. Run as Administrator
    echo 3. Clear npm cache: npm cache clean --force
    echo.
    pause
    exit /b 1
)

echo.
echo ✅ All dependencies installed!
echo.

echo Step 3: Starting React development server...
echo.
echo 🚀 Frontend will open at: http://localhost:3000
echo 📝 Demo accounts:
echo    Admin: admin / admin
echo    User:  user / user
echo.
echo ⚠️  Keep this window open while using the app
echo    Press Ctrl+C to stop the server
echo.
echo Starting in 3 seconds...
timeout /t 3 /nobreak >nul

npm start
