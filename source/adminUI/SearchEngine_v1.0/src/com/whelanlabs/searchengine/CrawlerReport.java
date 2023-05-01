/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.whelanlabs.searchengine;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.attribute.standard.DateTimeAtCompleted;

/**
 *
 * @author john
 */
public class CrawlerReport {

   public static String generateReport(String appRootDir) {

      StringBuffer text = new StringBuffer();
      StringBuffer urls = new StringBuffer();
      StringBuffer filters = new StringBuffer();
      FileInputStream fstream1 = null;
      FileInputStream fstream2 = null;
      FileInputStream fstream3 = null;
      String filtersFilename = null;
      String urlsFilename = null;
      String strLine;
      String previousLine = "";
      int errorCount = 0;
      ArrayList<String> errors = new ArrayList<String>();
      String finalDepth = "";

      try {
         
         String startTime = null;
         String endTime = null;
         Integer crawlLevel = 0;
         Integer totalSize = 0;
         HashMap<String, HashMap<Integer, Integer>> contextRoots = new HashMap<String, HashMap<Integer, Integer>>();
         ArrayList<AggregateResult> topResults = new ArrayList<AggregateResult>();
         HashMap<Integer, Integer> hitsPerlevel = new HashMap<Integer, Integer>();
         
         String logFilename = appRootDir + "/cygwin/whelanlabs/searchengine/crawl.log";
         fstream1 = new FileInputStream(logFilename);
         DataInputStream in1 = new DataInputStream(fstream1);
         BufferedReader br1 = new BufferedReader(new InputStreamReader(in1));

         while ((strLine = br1.readLine()) != null) {
            if(strLine.startsWith("fetching ")) {
//               System.out.println("strLine: " + strLine);
               int firstSpace = strLine.indexOf(" ");
               int firstColon = strLine.indexOf("://");
               int endOfHostAndPort = strLine.substring(firstColon+4).indexOf("/") + (firstColon+4);
               int endOfContext = strLine.substring(endOfHostAndPort+1).indexOf("/") + (endOfHostAndPort+1);
               
               String contextRoot = strLine.substring(firstSpace+1, endOfContext);
               HashMap<Integer, Integer> thisContextRoot = contextRoots.get(contextRoot);
               if(null==thisContextRoot) {
                  thisContextRoot = new HashMap<Integer, Integer>();
               }
               Integer count = thisContextRoot.get(crawlLevel);
               if(null==count) {
                  count=0;
               }
               count++;
               
               
               Integer numHits = hitsPerlevel.get(crawlLevel);
               if(null==numHits) {
                  numHits = 0;
               }
               numHits++;
               hitsPerlevel.put(crawlLevel, numHits);
               
               thisContextRoot.put(crawlLevel, count);
               contextRoots.put(contextRoot, thisContextRoot);
            }
            else if(strLine.startsWith("Fetcher: done")) {
               crawlLevel++;
            }
            else if(strLine.startsWith("start time: ")) {
               startTime = strLine.substring(strLine.indexOf(":")+1);
            }
            else if(strLine.startsWith("end time: ")) {
               endTime = strLine.substring(strLine.indexOf(":")+1);
            }
            else if(strLine.startsWith("maxFieldLength ")) {
               errorCount++;
               errors.add(previousLine + "\n" + strLine + "\n");
            }
            else if(strLine.contains(" failed with: ")) {
               errorCount++;
               errors.add(strLine + "\n");
            }
            else if(strLine.startsWith("Crawler depth set to ")) {
               finalDepth = strLine.substring(21);
            }
            
            previousLine = strLine;
            
         }

         // get the filter file contents
         try {
            filtersFilename = appRootDir + "/cygwin/whelanlabs/searchengine/filtersUsed.txt";
            fstream2 = new FileInputStream(filtersFilename);
            DataInputStream in2 = new DataInputStream(fstream2);
            BufferedReader br2 = new BufferedReader(new InputStreamReader(in2));
            while ((strLine = br2.readLine()) != null) {
               filters.append("<LI>   \"" + strLine + "\"</LI>\n");
            }
         } catch (java.io.FileNotFoundException ex) {
            filters.append("<LI>   [File not found. (" + filtersFilename + ")]</LI>\n");
         }

         // get the starting-point URLs file contents
         try {
            urlsFilename = appRootDir + "/cygwin/whelanlabs/searchengine/startingPointsUsed.txt";
            fstream3 = new FileInputStream(urlsFilename);
            DataInputStream in3 = new DataInputStream(fstream3);
            BufferedReader br3 = new BufferedReader(new InputStreamReader(in3));
            while ((strLine = br3.readLine()) != null) {
               urls.append("<LI>   \"" + strLine + "\"</LI>\n");
            }
         } catch (java.io.FileNotFoundException ex) {
            urls.append("<LI>   [File not found. (" + urlsFilename + ")]</LI>\n");
         }

         Collection c1 = contextRoots.keySet();
         Iterator itr1 = c1.iterator();
         while (itr1.hasNext()) {
            String key = (String) itr1.next();
            HashMap h2 = (HashMap) contextRoots.get(key);
            Collection c2 = h2.values();
            Iterator itr2 = c2.iterator();
            Integer size = 0;
            while (itr2.hasNext()) {
               size += (Integer) itr2.next();
            }
            totalSize += size;
            AggregateResult ar = new AggregateResult(key, size, contextRoots.get(key));
            topResults.add(ar);
         }
         
         text.append("<HTML><BODY> \n");
         text.append("<H1>WhelanLabs SearchEngine Manager - Crawler Report</H1>" + "\n");
         text.append("<HR>" + "\n");
         
         text.append("<H2>Start Time: " + startTime + "</H2>\n");
         if(null != endTime) {
            text.append("<H2>End Time: " + endTime + "</H2>\n");
            Date timeStart = new Date(startTime);
            Date timeEnd = new Date(endTime);
            long duration = (timeEnd.getTime() - timeStart.getTime())/1000 ;
            Integer hours = (int) (duration / 3600);
            int remainder = (int) (duration % 3600);
            Integer minutes = remainder / 60;
            Integer seconds = remainder % 60;
            text.append("<H2>Duration: " + hours + ":" + minutes + ":" + seconds + " (hours:minutes:seconds)</H2>\n\n");
         } else {
            text.append("<H2>End Time: [still running]"+ "</H2>\n\n");
         }
         
         text.append("<H2>Traversal Depth: " + crawlLevel + " of " + finalDepth + " levels</H2>\n\n");

         
         text.append("<H2>Number of Pages Crawled: </H2>\n");

         Object[] levels = hitsPerlevel.keySet().toArray();
         Arrays.sort(levels);
         int levelsLength = levels.length;
         text.append("<UL><TABLE border=2 cellpadding=3>\n");
         for (int i = 0; i < levelsLength; i++) {
            Integer level = (Integer) levels[i];
            Integer hitsOnLevel = hitsPerlevel.get(level);
            text.append("<TR><TD>   Level " + level + ": </TD><TD align=right>" + hitsOnLevel + " pages</TD></TR>\n");
         }
         text.append("<TR><TD><B> TOTAL:  </B></TD><TD align=right><B>" + totalSize + " pages</B></TD></TR>\n");
         text.append("</TABLE></UL>\n");

         text.append("<H2>Starting Point URLs: </H2>\n");
         text.append("<UL>\n");
         text.append(urls);
         text.append("</UL>\n");
         
         text.append("\n<H2>Filter Criteria: </H2>\n");
         text.append("<UL>\n");
         text.append(filters);
         text.append("</UL>\n");
         
         text.append("\n<H2>Top Results: </H2>\n");
         Comparator<AggregateResult> arc = new Comparator<AggregateResult>() {
            public int compare(AggregateResult ar1, AggregateResult ar2) {
               int answer = (ar1._size - ar2._size) * (-1);
               if (0 == answer) {
                  answer = ar1._key.compareTo(ar2._key);
               }
               return answer;
            }
            ;
         };
         
         Collections.sort(topResults, arc);
         Iterator iterator = topResults.iterator();
         text.append("<UL>\n");
         while (iterator.hasNext()) {
            AggregateResult ar = (AggregateResult) iterator.next();
            String goodEnglish = (ar._size==1) ? " hit @ " : " hits @ ";
            
            text.append("<LI>   " + ar._size + goodEnglish + ar._key + "/ " + ar._flow + "</LI>\n");
         }
         text.append("</UL>\n");
         
         text.append("\n<H2>Number of Errors: " + errorCount + "</H2>\n");

         if (errorCount > 0) {
            text.append("\n<H2>Reported Errors: </H2>\n");
            Iterator errorsIterator = errors.iterator();
            text.append("<UL>\n");
            while (errorsIterator.hasNext()) {
               text.append("<LI>   " + errorsIterator.next() + "</LI>");
            }
            text.append("</UL>\n");
         }
         
         text.append("</BODY></HTML>\n");
         
      } catch (Exception ex) {
         Logger.getLogger(CrawlerReport.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
         try {
            fstream1.close();
            fstream2.close();
            fstream3.close();
         } catch (Exception ex) {
            Logger.getLogger(CrawlerReport.class.getName()).log(Level.SEVERE, null, ex);
         }
         
         return text.toString();
      }
   }
   
   
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       System.out.println(CrawlerReport.generateReport("C:/WhelanLabs/SearchEngine"));
    }

}
