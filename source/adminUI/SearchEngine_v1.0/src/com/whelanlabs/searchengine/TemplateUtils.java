/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.whelanlabs.searchengine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author john
 */
public class TemplateUtils {

   public static  String replaceText(String sourcetext, String templatetext, String replacementtext) {
      return sourcetext.replaceAll(templatetext, replacementtext);
   }
   
   public static String getFileText(String filename) {
      BufferedReader reader = null;
      StringBuffer fileData = null;
      try {
         fileData = new StringBuffer(1000);
         reader = new BufferedReader(new FileReader(filename));
         char[] buf = new char[1024];
         int numRead = 0;
         while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
         }
         reader.close();
      } catch (IOException ex) {
         Logger.getLogger(TemplateUtils.class.getName()).log(Level.SEVERE, null, ex);
      }
      finally {
         try {
            reader.close();
         } catch (IOException ex) {
            Logger.getLogger(TemplateUtils.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
      return fileData.toString();
   }
   
   public static void setFileText(String text, String filename) {
      try {
         File outFile = new File(filename);
         FileWriter out = new FileWriter(outFile);
         out.write(text);
         out.close();
      } catch (IOException ex) {
         Logger.getLogger(TemplateUtils.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
   
   public static void updateNutchConfig(String appRootDirectory) {

      Properties proxyProperties = new Properties();
      Properties adminProperties = new Properties();
      try {
         String proxyPropFile = appRootDirectory + "/config/proxy.properties";
         proxyProperties.load(new FileInputStream(proxyPropFile));
         String adminPropFile = appRootDirectory + "/config/adminUI.properties";
         adminProperties.load(new FileInputStream(adminPropFile));
      } catch (IOException ex) {
         Logger.getLogger(TemplateUtils.class.getName()).log(Level.SEVERE, null, ex);
      }

      String templateFileName = appRootDirectory + "/cygwin/whelanlabs/searchengine/nutch/conf/nutch-site.xml.template";
      String outputFileName = appRootDirectory + "/cygwin/whelanlabs/searchengine/nutch/conf/nutch-site.xml";

      String text = TemplateUtils.getFileText(templateFileName);
      if ("true".equals(proxyProperties.getProperty("proxyEnabled"))) {
         text = TemplateUtils.replaceText(text, "@proxy_host@", proxyProperties.getProperty("proxyHost"));
      } else {
         text = TemplateUtils.replaceText(text, "@proxy_host@", "");
      }
      text = TemplateUtils.replaceText(text, "@proxy_port@", proxyProperties.getProperty("proxyPort"));
      text = TemplateUtils.replaceText(text, "@proxy_user@", proxyProperties.getProperty("proxyUser"));
      text = TemplateUtils.replaceText(text, "@proxy_password@", proxyProperties.getProperty("proxyPassword"));

      text = TemplateUtils.replaceText(text, "@organization@", adminProperties.getProperty("organizationName"));
      text = TemplateUtils.replaceText(text, "@nutch_site@", adminProperties.getProperty("organizationURL"));
      text = TemplateUtils.replaceText(text, "@nutch_admin_email@", adminProperties.getProperty("siteAdminEMail"));
      
      TemplateUtils.setFileText(text, outputFileName);
   }

   static void rewriteUrlFilter(String appRootDirectory) {
      
      String advancedFiltersPropertiesFile = appRootDirectory + "/config/advancedFilters.properties";
      String adminUIPropertiesFile = appRootDirectory + "/config/adminUI.properties";
      
      Properties adminUIProperties = new Properties();
      Properties advancedFiltersProperties = new Properties();
      try {
         adminUIProperties.load(new FileInputStream(adminUIPropertiesFile));
      } catch (IOException ex) {
         Logger.getLogger(TemplateUtils.class.getName()).log(Level.SEVERE, null, ex);
      }
      try {
         advancedFiltersProperties.load(new FileInputStream(advancedFiltersPropertiesFile));
      } catch (IOException ex) {
         Logger.getLogger(TemplateUtils.class.getName()).log(Level.SEVERE, null, ex);
      }



      StringBuffer urlFilter = new StringBuffer();
      
      String numFiltersString = (String) advancedFiltersProperties.get("numEntries");
      Integer numAdvancedFilters = Integer.valueOf(numFiltersString);
      for(int i = 0; i<numAdvancedFilters; i++) {
         urlFilter.append((String) advancedFiltersProperties.get("advancedFilter." + Integer.toString(i)));
         urlFilter.append("\n");
      }
      
      // # skip image and other suffixes we can't yet parse
      // -\.(gif|GIF|jpg|JPG|png|PNG|ico|ICO|css|sit|eps|wmf|zip|ppt|mpg|xls|gz|rpm|tgz|mov|MOV|exe|jpeg|JPEG|bmp|BMP)$
      urlFilter.append("-\\.(gif|GIF|jpg|JPG|png|PNG|ico|ICO|css|sit|eps|wmf|zip|ppt|mpg|xls|gz|rpm|tgz|mov|MOV|exe|jpeg|JPEG|bmp|BMP)$\n");
      
      // skipFTPSitesCheckBox
      if( "true".equals(adminUIProperties.getProperty("CrawlerSkipFTPSites")) )
      {
         Logger.getLogger(TemplateUtils.class.getName()).log(Level.INFO, ("skipFTPSitesCheckBox is selected."));
         urlFilter.append("-^ftp:\n");
         urlFilter.append("-^FTP:\n");
      }
      // skipLocalFilesCheckBox
      if( "true".equals(adminUIProperties.getProperty("CrawlerSkipLocalFiles")) )
      {
         Logger.getLogger(TemplateUtils.class.getName()).log(Level.INFO, ("skipLocalFilesCheckBox is selected."));
         urlFilter.append("-^file:\n");
         urlFilter.append("-^FILE:\n");
      }
      // skipDynamicURLsCheckBox
      if( "true".equals(adminUIProperties.getProperty("CrawlerSkipDynamicURLs")) )
      {
         Logger.getLogger(TemplateUtils.class.getName()).log(Level.INFO, ("skipDynamicURLsCheckBox is selected."));
         urlFilter.append("-[?*!@=]\n");
      }
      // skipEmailLinksCheckBox
      if( "true".equals(adminUIProperties.getProperty("CrawlerSkipEmailLinks")) )
      {
         Logger.getLogger(TemplateUtils.class.getName()).log(Level.INFO, ("skipEmailLinksCheckBox is selected."));
         urlFilter.append("-^mailto:\n");
         urlFilter.append("-^MAILTO:\n");
         urlFilter.append("-^MailTo:\n");
      }
      // CrawlerSkipProbableLoops
      if( "true".equals(adminUIProperties.getProperty("CrawlerSkipProbableLoops")) )
      {
         Logger.getLogger(TemplateUtils.class.getName()).log(Level.INFO, ("skipProbableLoopsCheckBox is selected."));
         urlFilter.append("-.*(/.+?)/.*?\\1/.*?\\1/.*?\\1/.*?\\1/\n");
      }

      String inclusionLevel = adminUIProperties.getProperty("CrawlerInclusionLevel");
      
      
      // 'Include URLs with starting-point domains'
      if ("1".equals(inclusionLevel)) // "Include URLs with starting-point domains."
      {
         Logger.getLogger(TemplateUtils.class.getName()).log(Level.INFO, ("Include URLs with starting-point domains."));
         urlFilter.append(getInclusionList("DOMAINS", appRootDirectory));
         urlFilter.append("-.\n");
      }
      // 'Include URLs with starting-point hosts'
      else if ("0".equals(inclusionLevel)) // "Include URLs with starting-point hosts".equals(selectedInclusionDomain))
      {
         Logger.getLogger(TemplateUtils.class.getName()).log(Level.INFO, ("Include URLs with starting-point hosts."));
         urlFilter.append(getInclusionList("HOSTS", appRootDirectory));
         urlFilter.append("-.\n");
      }
      // 'Include URLs with starting-point context roots'
      else if ("2".equals(inclusionLevel)) // "Include URLs with starting-point context roots".equals(selectedInclusionDomain))
      {
         Logger.getLogger(TemplateUtils.class.getName()).log(Level.INFO, ("Include URLs with starting-point context roots."));
         urlFilter.append(getInclusionList("CONTEXTROOTS", appRootDirectory));
         urlFilter.append("-.\n");
      }
      // 'Include all Hosts'
      else if ("3".equals(inclusionLevel)) // "Include all Hosts".equals(selectedInclusionDomain))
      {
         Logger.getLogger(TemplateUtils.class.getName()).log(Level.INFO, ("Include all Hosts."));
         urlFilter.append("+.\n");
      }
      else // include everything
      {
         Logger.getLogger(TemplateUtils.class.getName()).log(Level.INFO, ("No inclusion domain selecte, so include all Hosts."));
         urlFilter.append("+.\n");
      }
      
      Logger.getLogger(TemplateUtils.class.getName()).log(Level.INFO, ("urlFilter is: \n" + urlFilter));
      
      
      String filename = appRootDirectory + "/cygwin/whelanlabs/searchengine/nutch/conf/crawl-urlfilter.txt";
      BufferedWriter out = null;
      try {
         out = new BufferedWriter(new FileWriter(filename));
         out.write(urlFilter.toString());
      } catch (IOException ex) {
         Logger.getLogger(TemplateUtils.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
         try {
            out.close();
         } catch (IOException ex) {
            Logger.getLogger(TemplateUtils.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
   }

   private static String getInclusionList(String levelOfDetail, String appRootDirectory) {
      FileInputStream fstream = null;
      StringBuffer inclusionList = new StringBuffer();
      try {
         String filename = appRootDirectory + "/cygwin/whelanlabs/searchengine/urls/urls.txt";
         fstream = new FileInputStream(filename);
         // Get the object of DataInputStream
         DataInputStream in = new DataInputStream(fstream);
         BufferedReader br = new BufferedReader(new InputStreamReader(in));
         String strLine;

         //Read File Line By Line
         while ((strLine = br.readLine()) != null) {
            String currentURL = strLine;
            Logger.getLogger(TemplateUtils.class.getName()).log(Level.INFO, "currentURL = " + currentURL);
            String inclusion = "";
            if ("CONTEXTROOTS".equals(levelOfDetail)) {
               int startpoint = currentURL.indexOf("://");
               if (-1 == startpoint) {
                  startpoint = -3;
               }
               int endpoint = currentURL.indexOf("/", startpoint + 3);
               endpoint = currentURL.indexOf("/", endpoint + 1);
               if ((-1 == endpoint) || (endpoint <= startpoint + 3)) {
                  inclusion = currentURL.substring(startpoint + 3);
               } else {
                  inclusion = currentURL.substring(startpoint + 3, endpoint);
               }
            } else if ("HOSTS".equals(levelOfDetail)) {
               int startpoint = currentURL.indexOf("://");
               if (-1 == startpoint) {
                  startpoint = -3;
               }
               int endpoint = currentURL.indexOf(":", startpoint + 3);
               if (-1 != endpoint) {
                  inclusion = currentURL.substring(startpoint + 3, endpoint);
               } else if (currentURL.indexOf("/", startpoint + 3) > 0) {
                  endpoint = currentURL.indexOf("/", startpoint + 3);
                  inclusion = currentURL.substring(startpoint + 3, endpoint);
               } else {
                  inclusion = currentURL.substring(startpoint + 3);
               }
            } else if ("DOMAINS".equals(levelOfDetail)) {
               int startpoint = currentURL.indexOf("://");
               if (-1 == startpoint) {
                  startpoint = -3;
               }
               int testPoint = currentURL.indexOf("/", startpoint + 3);
               int endpoint = currentURL.indexOf(".", startpoint + 3);
               if ((-1 == testPoint) && (-1 == endpoint)) {
                  inclusion = currentURL.substring(startpoint+3);
               } else if (-1 == testPoint) {
                  inclusion = currentURL.substring(endpoint);
               } else if (endpoint < testPoint) {
                  inclusion = currentURL.substring(endpoint, testPoint);
               } else {
                  // no domain losted, so just go with the host.
                  inclusion = currentURL.substring(startpoint + 3, testPoint);
               }
            }
            inclusionList.append("+" + inclusion + "\n");
         }

         //Close the input stream
         in.close();


      } catch (IOException ex) {
         Logger.getLogger(TemplateUtils.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
         try {
            fstream.close();
         } catch (IOException ex) {
            Logger.getLogger(TemplateUtils.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
      return inclusionList.toString();
   }
}
