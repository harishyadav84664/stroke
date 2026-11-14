@echo off
echo ===================================================
echo   NIDUS - Neuroimaging Stroke Identification Model
echo   Clinical Reading Room Workstation (MySQL Mode)
echo ===================================================
echo.
echo Starting application with MySQL on localhost:3306/stroke_nidus...
echo Ensure MySQL service is running. Set MYSQL_USER / MYSQL_PASSWORD environment variables if needed.
echo Access NIDUS in your browser at: http://localhost:8080
echo.

start "" "http://localhost:8080"
java -jar "%~dp0target\app-dashboard-0.1.0.jar" --spring.profiles.active=mysql

pause
