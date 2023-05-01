#!/bin/sh
  
  rm /whelanlabs/searchengine/crawl.log
  touch /whelanlabs/searchengine/crawl.log
  /whelanlabs/searchengine/reindexNutchImpl.sh >& /whelanlabs/searchengine/crawl.log &
