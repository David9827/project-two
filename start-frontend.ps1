Write-Host "================================" -ForegroundColor Cyan
Write-Host "   Real Estate Management System" -ForegroundColor Cyan  
Write-Host "================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "Checking Node.js installation..." -ForegroundColor Yellow
try {
    $nodeVersion = node -v
    $npmVersion = npm -v
    Write-Host "✅ Node.js is installed" -ForegroundColor Green
    Write-Host "Node.js version: $nodeVersion" -ForegroundColor Gray
    Write-Host "npm version: $npmVersion" -ForegroundColor Gray
} catch {
    Write-Host "❌ Node.js is not installed or not in PATH!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please install Node.js first:" -ForegroundColor Yellow
    Write-Host "1. Go to: https://nodejs.org/" -ForegroundColor White
    Write-Host "2. Download and install Node.js LTS version" -ForegroundColor White
    Write-Host "3. Restart PowerShell" -ForegroundColor White
    Write-Host "4. Run this script again" -ForegroundColor White
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host ""
Write-Host "Installing dependencies..." -ForegroundColor Yellow
try {
    npm install
    Write-Host "✅ Dependencies installed successfully!" -ForegroundColor Green
} catch {
    Write-Host "❌ Failed to install dependencies!" -ForegroundColor Red
    Write-Host "Please check your internet connection and try again." -ForegroundColor Yellow
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host ""
Write-Host "Starting React development server..." -ForegroundColor Yellow
Write-Host "The application will open at: http://localhost:3000" -ForegroundColor Cyan
Write-Host ""
Write-Host "Demo accounts:" -ForegroundColor Green
Write-Host "  Admin: admin / admin" -ForegroundColor White
Write-Host "  User:  user / user" -ForegroundColor White
Write-Host ""
Write-Host "Press Ctrl+C to stop the server" -ForegroundColor Yellow
Write-Host ""

npm start
