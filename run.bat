
@echo off
setlocal

cd /d "%~dp0"

echo ====================================
echo      STUDENT EXPENSE TRACKER
echo ====================================
echo.

if not exist "config.properties" (
    echo ERROR: config.properties was not found.
    echo Copy config.properties.example to config.properties
    echo and enter your local MySQL credentials.
    pause
    exit /b 1
)

if not exist "lib\mysql-connector-j-26.7.0.jar" (
    echo ERROR: MySQL Connector/J was not found in the lib folder.
    pause
    exit /b 1
)

echo Compiling the application...
echo.

javac -cp "lib\mysql-connector-j-26.7.0.jar;src" -d src src\Main.java

if errorlevel 1 (
    echo.
    echo Compilation failed. Check the error messages above.
    pause
    exit /b 1
)

echo.
echo Starting Student Expense Tracker...
echo.

java -cp "src;lib\mysql-connector-j-26.7.0.jar" Main

echo.
pause
