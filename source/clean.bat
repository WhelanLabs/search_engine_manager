"@cygwin_dir@\bin\bash.exe" --login /whelanlabs/searchengine/stopCrawl.sh
del cygwin\whelanlabs\searchengine\CrawlInProgress.lock
del cygwin\whelanlabs\searchengine\NewCrawlAvailable.lock
rmdir /S /Q cygwin\whelanlabs\searchengine\crawls
mkdir cygwin\whelanlabs\searchengine\crawls


