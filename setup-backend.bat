@echo off
echo ====================================
echo   Real Estate Backend Setup
echo ====================================
echo.

echo Checking Java installation...
java -version
if %errorlevel% neq 0 (
    echo.
    echo ❌ Java is not installed or not in PATH!
    echo.
    echo Please install JDK 17 or higher:
    echo 1. Download from: https://www.oracle.com/java/technologies/downloads/
    echo 2. Or use OpenJDK: https://adoptopenjdk.net/
    echo 3. Make sure to add Java to your PATH
    echo.
    echo After installation, restart command prompt and run this script again.
    pause
    exit /b 1
)

echo.
echo ✅ Java is installed!
echo.

echo Checking Maven installation...
mvn -version
if %errorlevel% neq 0 (
    echo.
    echo ❌ Maven is not installed or not in PATH!
    echo.
    echo Maven is required to build the project.
    echo You can:
    echo 1. Install Maven: https://maven.apache.org/download.cgi
    echo 2. Or use the included Maven wrapper (mvnw.cmd)
    echo.
    echo Trying to use Maven wrapper...
    if exist mvnw.cmd (
        echo ✅ Found Maven wrapper, using mvnw.cmd
        set MAVEN_CMD=mvnw.cmd
    ) else (
        echo ❌ Maven wrapper not found
        echo Please install Maven or fix the issue
        pause
        exit /b 1
    )
) else (
    echo ✅ Maven is installed!
    set MAVEN_CMD=mvn
)

echo.
echo Building the project...
echo.
%MAVEN_CMD% clean compile

if %errorlevel% neq 0 (
    echo.
    echo ❌ Build failed!
    echo Please check the error messages above.
    pause
    exit /b 1
)

echo.
echo ✅ Build successful!
echo.
echo Starting Spring Boot application...
echo Backend will be available at: http://localhost:8080
echo Health check: http://localhost:8080/api/health
echo.
echo Press Ctrl+C to stop the server
echo.
%MAVEN_CMD% spring-boot:run
