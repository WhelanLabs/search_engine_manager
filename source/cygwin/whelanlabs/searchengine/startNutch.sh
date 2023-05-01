#!/bin/sh

dos2unix /whelanlabs/searchengine/settings.sh
source /whelanlabs/searchengine/settings.sh
crawlDir="crawl"$crawlIndex

if [ -f /whelanlabs/searchengine/NewCrawlAvailable.lock ] ; then
  rm /whelanlabs/searchengine/NewCrawlAvailable.lock
fi

CATALINA_HOME=/whelanlabs/searchengine/tomcat/
cd /whelanlabs/searchengine/crawls/$crawlDir
/whelanlabs/searchengine/tomcat/bin/startup.sh &