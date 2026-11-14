@echo off
echo ===================================================
echo   NIDUS - Neuroimaging Stroke Identification Model
echo   Clinical Reading Room Workstation
echo ===================================================
echo.
echo Starting application with embedded H2 database (zero-configuration)...
echo Access NIDUS in your browser at: http://localhost:8080
echo H2 Console available at: http://localhost:8080/h2-console
echo.

start "" "http://localhost:8080"
java -jar "%~dp0target\app-dashboard-0.1.0.jar" --spring.profiles.active=h2

pause
