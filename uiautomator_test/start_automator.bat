@echo off

����REM �����ӳٻ���������չ
����setlocal enabledelayedexpansion 

rem ǧ��ע�ⲻҪ��set����ʱʹ�õ�ϵͳkeyword�� path

echo -----------------########  ����build #######-----------------------------
call android create uitest-project -n uiautomator_test -t 4 -p %~dp0

echo.
echo -----------------#######  ��ʼ��� ########-----------------------------

cd %~dp0
%~d0
call ant build

adb disconnect
adb connect 192.166.66.241
adb push bin\uiautomator_test.jar /data/local/tmp/
adb shell uiautomator runtest uiautomator_test.jar -c test.android.dvb.ChannelSearch_Smoke