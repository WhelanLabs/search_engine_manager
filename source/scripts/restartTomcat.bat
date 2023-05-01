rem @echo off

set basedir=%1
shift

if "%1"=="" goto bottom
:top
set basedir=%basedir% %1
shift
if not "%1"=="" goto top
:bottom

call "%basedir%\scripts\vars.bat"
set fullpath=%basedir%\cygwin\whelanlabs\searchengine
set CATALINA_HOME=%basedir%\cygwin\whelanlabs\searchengine\tomcat
set JRE_HOME=%jredir%

"%cygwindir%\bin\bash" --login /whelanlabs/searchengine/startNutch.sh

