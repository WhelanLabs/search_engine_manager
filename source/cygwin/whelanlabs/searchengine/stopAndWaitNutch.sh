#!/bin/sh
echo shutting down Tomcat...
/whelanlabs/searchengine/tomcat/bin/shutdown.sh 2> /dev/null &
ping 1.1.1.1 -n 1 -w 30000 > nul