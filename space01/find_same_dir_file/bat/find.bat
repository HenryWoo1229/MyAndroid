@echo off
setlocal enabledelayedexpansion

for /f "delims=" %%i in ('dir /ad /b /s "%~d0"')do ( %��ȡ��ǰ������Ŀ¼����Ŀ¼·��%
	echo %%i
)
echo ---------------------

for /f "delims=" %%i in ('dir /s /a-d /b *.*')do ( %��ȡ��ǰ������Ŀ¼�����ļ�·��%
	echo %%i
)
echo ---------------------
for /f "delims=" %%i in ('dir /b /a-d /o-d')do ( %��ȡ��ǰ��������Ŀ¼�����ļ���%
	echo %%i
)
echo ---------------------
for /f "delims=" %%i in ('dir /b /ad /o-d')do ( %��ȡ��ǰ������Ŀ¼����Ŀ¼��%
	echo %%i
	set sub=%%i
	for /f "delims=" %%j in ('dir /b /ad /o-d !sub!')do echo %%j
)
echo ---------------------last
for /f "delims=" %%i in ('dir /b /a-d /o-d')do echo %%i   %��ȡ��ǰ������Ŀ¼�����ļ���%
for /f "delims=" %%i in ('dir /ad /b /s "%~d0"')do ( 
	set sub=%%i
	rem echo !sub!
	cd !sub!
	for /f "delims=" %%j in ('dir /b /a-d /o-d')do (
		if exist %%j echo %%j
	)
)