@echo off
REM ========================================
REM CafeFlow - Run Script
REM ========================================

echo.
echo ========================================
echo   CafeFlow POS System - Starting...
echo ========================================
echo.

echo Starting application...
echo Please wait...
echo.

call mvn exec:java -Dexec.mainClass="com.cafeflow.MainApp"

echo.
echo ========================================
echo   Application Closed
echo ========================================
echo.

pause
