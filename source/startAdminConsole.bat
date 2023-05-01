@echo off
rem WhelanLabs Search Engine version @version@

set basedir=@install_dir@
set jredir=@jre_dir@
set cygwindir=@cygwin_dir@

set spacelessID=%USERNAME: =~%
if "%spacelessID%"=="%USERNAME%" goto id_ok
echo ### ERROR: Invalid user name.
echo ### 
echo ### This application requires that the running user not have any whitespace
echo ### characters in their user name.
echo ### 
echo ### Press any key to exit.
pause>nul
goto end
:id_ok

for /f "tokens=*" %%a in ('"cscript "%basedir%\scripts\adminGroup.vbs""') do set ADMIN_GROUP=%%a

ver | find " 5."
if errorlevel 1 goto NotXP
net localgroup %ADMIN_GROUP% | find "%USERNAME%"
if errorlevel 1 goto xp_permission_error
goto run

:NotXP
ver | find " 6."
if errorlevel 1 goto version_error
rem The following line is a horrible hack, but it should work for all locales.
whoami /groups | find "S-1-5-32-544" | find ", "
if errorlevel 1 goto vista_permission_error
goto run

:version_error
echo ### ERROR: Non-supported Windows version.
echo ### 
echo ### This application requires Microst Windows XP or Vista to run.
echo ### 
echo ### Press any key to exit.
pause>nul
goto end

:xp_permission_error
echo ### ERROR: Insufficient permissions.
echo ### 
echo ### This application requires administrative permissions to run.
echo ### 
echo ### Press any key to exit.
pause>nul
goto end

:vista_permission_error
echo ### ERROR: Insufficient permissions.
echo ### 
echo ### This application requires administrative permissions to run. For systems
echo ### with User Access Control (UAC) enabled, be sure to start this application
echo ### by clicking the right mouse button, and selecting 'Run as administrator'.
echo ### 
echo ### Press any key to exit.
pause>nul
goto end


:run

rem set additionalArgs=-Xdebug -Xrunjdwp:transport=dt_socket,address=7777,server=y,suspend=n
set additionalArgs=

set classpath=%basedir%\adminUI\SearchEngine_v1.0\dist\SearchEngine_v1.0.jar;%basedir%\adminUI\SearchEngine_v1.0\dist\lib\appframework-1.0.3.jar;%basedir%\adminUI\SearchEngine_v1.0\dist\lib\swing-worker-1.1.jar

set JAVA_HOME=%jredir%

start "WhelanLabs SearchEngine Admin Console" "%jredir%\bin\javaw" -DcygwinDirectory="%cygwindir%" -DappRootDirectory="%basedir%" %additionalArgs% com.whelanlabs.searchengine.AdminUI 

:end