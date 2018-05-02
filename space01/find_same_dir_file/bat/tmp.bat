@echo off
setlocal enabledelayedexpansion

if exist "dirs.txt" del dirs.txt
if exist "files.txt" del dirs.txt

for /f "delims=" %%i in ('dir /b /ad /o-d')do ( %获取当前包括子目录所有目录名%
	echo %%i > dirs.txt
	set sub=%%i
	for /f "delims=" %%j in ('dir /b /ad /o-d !sub!')do echo %%j >> dirs.txt
)
echo ---------------------last
for /f "delims=" %%i in ('dir /b /a-d /o-d')do echo %%i >files.txt
for /f "delims=" %%i in ('dir /ad /b /s "%~d0"')do ( %获取当前包括子目录所有目录路径%
	set sub=%%i
	rem echo !sub!
	cd !sub!
	for /f "delims=" %%j in ('dir /b /a-d /o-d')do (
		echo %%j >>files.txt
	)
)