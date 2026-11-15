@echo off
chcp 65001 > nul
rem Compila el proyecto y ejecuta: ejecutar.bat [pruebas]
if exist out rmdir /s /q out
dir /s /b src\*.java test\*.java > fuentes.txt
javac -encoding UTF-8 -d out @fuentes.txt
del fuentes.txt
if errorlevel 1 exit /b 1
if /i "%1"=="pruebas" (
  java -cp out PruebasTaller
) else (
  java -cp out taller.SistemaTaller
)
