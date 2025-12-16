@echo off
REM ========================================
REM CafeFlow - Test Script
REM ========================================

echo.
echo ========================================
echo   CafeFlow POS System - Running Tests
echo ========================================
echo.

echo Running unit tests...
echo.

call mvn test

echo.
echo ========================================
echo   Tests Complete!
echo ========================================
echo.

pause
