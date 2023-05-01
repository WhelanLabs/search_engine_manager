rem @echo off

rem set basedir=C:\sandbox\WhelanLabs_Search_Engine\source
set fullpath=%basedir%\cygwin\whelanlabs\searchengine
set CATALINA_HOME=%basedir%\tomcat
set JRE_HOME=%jrehome%\jre1.6.0_07

%basedir%\cygwin\bin\bash /whelanlabs/searchengine/startNutch.sh