@echo off
setlocal enabledelayedexpansion

for /f "delims=" %%i in ('dir /ad /b /s "%~d0"')do ( %获取当前包括子目录所有目录路径%
	echo %%i
)
echo ---------------------

for /f "delims=" %%i in ('dir /s /a-d /b *.*')do ( %获取当前包括子目录所有文件路径%
	echo %%i
)
echo ---------------------
for /f "delims=" %%i in ('dir /b /a-d /o-d')do ( %获取当前不包括子目录所有文件名%
	echo %%i
)
echo ---------------------
for /f "delims=" %%i in ('dir /b /ad /o-d')do ( %获取当前包括子目录所有目录名%
	echo %%i
	set sub=%%i
	for /f "delims=" %%j in ('dir /b /ad /o-d !sub!')do echo %%j
)
echo ---------------------last
for /f "delims=" %%i in ('dir /b /a-d /o-d')do echo %%i   %获取当前包括子目录所有文件名%
for /f "delims=" %%i in ('dir /ad /b /s "%~d0"')do ( 
	set sub=%%i
	rem echo !sub!
	cd !sub!
	for /f "delims=" %%j in ('dir /b /a-d /o-d')do (
		if exist %%j echo %%j
	)
)