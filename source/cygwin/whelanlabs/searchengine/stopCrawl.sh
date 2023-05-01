#!/bin/sh

  if [ -f /whelanlabs/searchengine/CrawlInProgress.lock ] ; then
  
    dos2unix /whelanlabs/searchengine/settings.sh
    source /whelanlabs/searchengine/settings.sh
    dos2unix /whelanlabs/searchengine/CrawlInProgress.lock
    source /whelanlabs/searchengine/CrawlInProgress.lock

    bothPid=$(ps -ae | grep java | grep $crawlPID | cut -c1-18)
    echo kill pids \'$bothPid\'
    kill -9 $bothPid
    rm /whelanlabs/searchengine/CrawlInProgress.lock
    incremented=$(($crawlIndex + 1))
    newDir="crawl"$incremented
    if [ -d /whelanlabs/searchengine/crawls/$newDir ] ; then
      rm -R /whelanlabs/searchengine/crawls/$newDir
    fi
  fi