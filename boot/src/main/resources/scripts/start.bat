@echo off
title %app_name%
SET PORT=9998
SET MAIN_CLASS=com.meizu.simplify.bootstrap.Server

SET APP_HOME=%~dp0
SET LOG_DIR=%APP_HOME%logs

echo JAVA_HOME=%JAVA_HOME%
SET JAVA_EXE="%JAVA_HOME%\bin\java.exe"

SET CLASSPATH=.
FOR %%F IN (..\lib\*.jar) DO call :ADDCP %%F
goto RUN

:ADDCP
set CLASSPATH=%CLASSPATH%;%1
goto :EOF 

:RUN
echo %CLASSPATH%
echo %APP_HOME%
echo %LOG_DIR%
echo %JAVA_EXE% -DlogDir=%LOG_DIR% %MAIN_CLASS% %PORT% -classpath %CLASSPATH% 
%JAVA_EXE% -DlogDir=%LOG_DIR% %MAIN_CLASS% %PORT% -classpath %CLASSPATH% 

CMD
