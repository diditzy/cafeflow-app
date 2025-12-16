@echo off
REM ========================================
REM CafeFlow - Full Build & Run Script
REM ========================================

echo.
echo ========================================
echo   CafeFlow POS System - Full Build
echo ========================================
echo.

echo [1/4] Cleaning...
call mvn clean

echo.
echo [2/4] Compiling...
call mvn compile

echo.
echo [3/4] Running tests...
call mvn test

echo.
echo [4/4] Starting application...
call mvn exec:java -Dexec.mainClass="com.cafeflow.MainApp"

echo.
echo ========================================
echo   Done!
echo ========================================
echo.

pause
