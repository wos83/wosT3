@echo off
setlocal

set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.7.6-hotspot"
set "ANDROID_HOME=C:\Users\User\AppData\Local\Android\Sdk"
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo Java: %JAVA_HOME%
echo Android SDK: %ANDROID_HOME%
echo.

call gradlew.bat assembleDebug --no-daemon

endlocal