#!/bin/sh

# /whelanlabs/searchengine/crawl.exe is linked to /bin/sh.exe. The link is used
# to help indetify the process for asynchronous halting.

cd /whelanlabs/searchengine
if [ -f /whelanlabs/searchengine/CrawlInProgress.lock ] ; then
  echo "A crawl is already in progress."
  exit 1
else
  if [ -f /whelanlabs/searchengine/NewCrawlAvailable.lock ] ; then
    rm /whelanlabs/searchengine/NewCrawlAvailable.lock
  fi
  echo \#\!/bin/sh > /whelanlabs/searchengine/CrawlInProgress.lock
  echo crawlPID=$$ >> /whelanlabs/searchengine/CrawlInProgress.lock

  echo 
  echo -n "start time: "
  date

  cp /whelanlabs/searchengine/urls/urls.txt /whelanlabs/searchengine/startingPointsUsed.txt
  cp /whelanlabs/searchengine/nutch/conf/crawl-urlfilter.txt /whelanlabs/searchengine/filtersUsed.txt

  dos2unix /whelanlabs/searchengine/settings.sh
  source /whelanlabs/searchengine/settings.sh
  
  dos2unix /whelanlabs/searchengine/depth.sh
  source /whelanlabs/searchengine/depth.sh
  
  depth="-depth $crawlerDepth"
  
  incremented=$(($crawlIndex + 1))
  newDir="crawl"$incremented

  echo Initiating crawl in $newDir
  echo Crawler depth set to $crawlerDepth
  echo 
  
  cd /whelanlabs/searchengine/crawls
  mkdir $newDir
  cd $newDir
  cp -R /whelanlabs/searchengine/urls ./
  /whelanlabs/searchengine/nutch/bin/nutch crawl urls -dir crawl $depth
  
  echo "#!/bin/sh" > /whelanlabs/searchengine/settings.sh
  echo "crawlIndex=$incremented" >> /whelanlabs/searchengine/settings.sh

  echo 
  echo Removing old crawls...
  echo 
  
  upperLimit=$(($crawlIndex - 1))
  for ((i=1;i<=$upperLimit;i+=1)); do
    dirToDelete="crawl"$i
    if [ -d /whelanlabs/searchengine/crawls/$dirToDelete ] ; then
      echo deleting /whelanlabs/searchengine/crawls/$dirToDelete ...
      rm -R /whelanlabs/searchengine/crawls/$dirToDelete
    fi
  done

  echo 
  echo -n "end time: "
  date
  echo 
  echo "New crawl available." > /whelanlabs/searchengine/NewCrawlAvailable.lock
  rm /whelanlabs/searchengine/CrawlInProgress.lock
fi
exit 0

