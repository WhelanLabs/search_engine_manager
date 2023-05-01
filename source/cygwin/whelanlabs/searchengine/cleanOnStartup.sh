#!/bin/sh

  if [ -f /whelanlabs/searchengine/CrawlInProgress.lock ] ; then
  
    dos2unix /whelanlabs/searchengine/settings.sh
    source /whelanlabs/searchengine/settings.sh
    dos2unix /whelanlabs/searchengine/CrawlInProgress.lock
    source /whelanlabs/searchengine/CrawlInProgress.lock

    isLive=$(ps -ae | grep java | grep $crawlPID)
    
    if [ "$isLive" = "" ] ; then
      rm /whelanlabs/searchengine/CrawlInProgress.lock
      incremented=$(($crawlIndex + 1))
      newDir="crawl"$incremented
      if [ -d /whelanlabs/searchengine/crawls/$newDir ] ; then
        rm -R /whelanlabs/searchengine/crawls/$newDir
      fi
    fi
  fi