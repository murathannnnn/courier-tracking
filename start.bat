@echo off
cd /d %~dp0

echo ======================================
echo  Courier Tracking System Starting...
echo ======================================

docker info >nul 2>&1
IF %ERRORLEVEL% NEQ 0 (
    echo.
    echo Docker Desktop is not running.
    echo Please start Docker Desktop and try again.
    pause
    exit /b
)

docker compose up --build

pause

