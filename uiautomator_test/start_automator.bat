@echo off

　　REM 启用延迟环境变量扩展
　　setlocal enabledelayedexpansion 

rem 千万注意不要在set变量时使用到系统keyword： path

echo -----------------########  生成build #######-----------------------------
call android create uitest-project -n uiautomator_test -t 4 -p %~dp0

echo.
echo -----------------#######  开始打包 ########-----------------------------

cd %~dp0
%~d0
call ant build

adb disconnect
adb connect 192.166.66.241
adb push bin\uiautomator_test.jar /data/local/tmp/
adb shell uiautomator runtest uiautomator_test.jar -c test.android.dvb.ChannelSearch_Smoke