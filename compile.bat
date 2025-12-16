@echo off
REM ========================================
REM CafeFlow - Compile Script
REM ========================================

echo.
echo ========================================
echo   CafeFlow POS System - Compile
echo ========================================
echo.

echo [1/2] Cleaning previous build...
call mvn clean

echo.
echo [2/2] Compiling project...
call mvn compile

echo.
echo ========================================
echo   Compilation Complete!
echo ========================================
echo.
echo To run the application, execute: run.bat
echo To run tests, execute: test.bat
echo.

pause
