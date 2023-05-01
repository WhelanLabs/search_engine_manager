/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.whelanlabs.searchengine;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author john
 */
public class CrawlerMonitor implements Runnable {
   private AdminUIView view;
   private String appRootDir;

   public CrawlerMonitor(AdminUIView theView, String appRootDirectory) {
      this.view = theView;
      this.appRootDir = appRootDirectory;
   }
   
   public void run() {
      File crawlInProgress = new File(appRootDir + "/cygwin/whelanlabs/searchengine/CrawlInProgress.lock");
      File newCrawlAvaliable = new File(appRootDir + "/cygwin/whelanlabs/searchengine/NewCrawlAvailable.lock");
      while(true) {
         if(newCrawlAvaliable.exists()) {
            view.updateCrawlStatusTextField("New Crawl Available");
         }
         else if(crawlInProgress.exists()) {
            view.updateCrawlStatusTextField("Crawl in Progress");
         }
         else {
            view.updateCrawlStatusTextField("Crawl is Up-To-Date");
         }
         try {
            Thread.sleep(5000);
         } catch (InterruptedException ex) {
            Logger.getLogger(CrawlerMonitor.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
   }

}
