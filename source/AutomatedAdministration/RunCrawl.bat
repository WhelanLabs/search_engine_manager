

set basedir=@base_dir@
call "%basedir%\scripts\vars.bat"
set JAVA_HOME=%jredir%
"%cygwindir%/bin/bash.exe" --login /whelanlabs/searchengine/reindexNutchAndWait.sh
"%cygwindir%/bin/bash.exe" --login /whelanlabs/searchengine/stopAndWaitNutch.sh
"%cygwindir%/bin/bash.exe" --login /whelanlabs/searchengine/startNutch.sh