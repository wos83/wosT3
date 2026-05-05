@echo off
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.7.6-hotspot
set ANDROID_HOME=C:\Users\User\AppData\Local\Android\Sdk
cd /d C:\Development\WOS\tic-tac-toe
call gradlew.bat assembleDebug --no-daemon