/*
 * AdminUIView.java
 */

package com.whelanlabs.searchengine;

import java.awt.Cursor;
import java.awt.Desktop;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ListModel;
import org.jdesktop.application.Action;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.net.URI;
import java.util.Properties;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * The application's main frame.
 */
public class AdminUIView extends FrameView {
   private Properties properties = null;
   private String cygwinDirectory = null;
   private String enginePort = null;

    public AdminUIView(SingleFrameApplication app) {
        super(app);

        //getFrame().setResizable(false);

        initComponents();
        
        loadConfiguration();

    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = AdminUI.getApplication().getMainFrame();
            aboutBox = new AdminUIAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        AdminUI.getApplication().show(aboutBox);
    }




   private void restartEngine() throws InterruptedException, InterruptedException, IOException {
      stopSearchEngine();
      Thread.sleep(5000);
      String filename = "\"" + cygwinDirectory + "/bin/bash.exe\" --login /whelanlabs/searchengine/startNutch.sh";
      Process p = Runtime.getRuntime().exec(filename);
      p.waitFor();
   }

   public synchronized void rewriteUrlFilter() {
      StringBuffer urlFilter = new StringBuffer();
      
      properties.setProperty("CrawlerSkipFTPSites", "false");
      properties.setProperty("CrawlerSkipLocalFiles", "false");
      properties.setProperty("CrawlerSkipDynamicURLs", "false");
      properties.setProperty("CrawlerSkipEmailLinks", "false");
      

      // skipFTPSitesCheckBox
      if( skipFTPSitesCheckBox.isSelected())
      {
         properties.setProperty("CrawlerSkipFTPSites", "true");
         Logger.getLogger(AdminUIView.class.getName()).log(Level.INFO, ("skipFTPSitesCheckBox is selected."));
      }
      // skipLocalFilesCheckBox
      if( skipLocalFilesCheckBox.isSelected())
      {
         properties.setProperty("CrawlerSkipLocalFiles", "true");
         Logger.getLogger(AdminUIView.class.getName()).log(Level.INFO, ("skipLocalFilesCheckBox is selected."));
      }
      // skipDynamicURLsCheckBox
      if( skipDynamicURLsCheckBox.isSelected())
      {
         properties.setProperty("CrawlerSkipDynamicURLs", "true");
         Logger.getLogger(AdminUIView.class.getName()).log(Level.INFO, ("skipDynamicURLsCheckBox is selected."));
      }
      // skipEmailLinksCheckBox
      if( skipEmailLinksCheckBox.isSelected())
      {
         properties.setProperty("CrawlerSkipEmailLinks", "true");
         Logger.getLogger(AdminUIView.class.getName()).log(Level.INFO, ("skipEmailLinksCheckBox is selected."));
      }
      if( skipProbableLoopsCheckBox.isSelected())
      {
         properties.setProperty("CrawlerSkipProbableLoops", "true");
         Logger.getLogger(AdminUIView.class.getName()).log(Level.INFO, ("skipEmailLinksCheckBox is selected."));
      }
      
      
      // inclusionDomainComboBox
      String selectedInclusionDomain = "";
      try {
         selectedInclusionDomain = inclusionDomainComboBox.getSelectedItem().toString();
      }
      catch (NullPointerException npe) {
         // do nothing.
      }
      properties.setProperty("CrawlerInclusionLevel", Integer.toString(inclusionDomainComboBox.getSelectedIndex()));
      
      // 'Include URLs with starting-point domains'
      if ("Include URLs with starting-point domains".equals(selectedInclusionDomain))
      {
         Logger.getLogger(AdminUIView.class.getName()).log(Level.INFO, ("Include URLs with starting-point domains."));
      }
      // 'Include URLs with starting-point hosts'
      else if ("Include URLs with starting-point hosts".equals(selectedInclusionDomain))
      {
         Logger.getLogger(AdminUIView.class.getName()).log(Level.INFO, ("Include URLs with starting-point hosts."));
      }
      // 'Include URLs with starting-point context roots'
      else if ("Include URLs with starting-point context roots".equals(selectedInclusionDomain))
      {
         Logger.getLogger(AdminUIView.class.getName()).log(Level.INFO, ("Include URLs with starting-point context roots."));
      }
      // 'Include all Hosts'
      else if ("Include all Hosts".equals(selectedInclusionDomain))
      {
         Logger.getLogger(AdminUIView.class.getName()).log(Level.INFO, ("Include all Hosts."));
      }
      else // include everything
      {
         Logger.getLogger(AdminUIView.class.getName()).log(Level.INFO, ("No inclusion domain selecte, so include all Hosts."));
      }
      
      Logger.getLogger(AdminUIView.class.getName()).log(Level.INFO, ("urlFilter is: \n" + urlFilter));
      
      try {
         String filename = appRootDirectory + "/config/adminUI.properties";
         properties.store(new FileOutputStream(filename), null);
      } catch (IOException ex) {
         Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, ex);
      }

      ListModel model = rootURLsList.getModel();
      int modelSize = model.getSize();
      String[] rootURLsArray = new String[modelSize];
      for( int i=0; i < modelSize; i++) {
         rootURLsArray[i] = (String) model.getElementAt(i);
      }
      
      TemplateUtils.rewriteUrlFilter(appRootDirectory);
   }
    


   private String replaceText(String sourcetext, String templatetext, String replacementtext) {
      return sourcetext.replaceAll(templatetext, replacementtext);
   }
   
   
   private void generateFile(String templateFileName, String outputFileName, String inValue, String outValue) {
      String inputtext = TemplateUtils.getFileText(templateFileName);
      String replacedtext = replaceText(inputtext, inValue, outValue);
      TemplateUtils.setFileText(replacedtext, outputFileName);
   }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents() {

      mainPanel = new javax.swing.JPanel();
      jTabbedPane1 = new javax.swing.JTabbedPane();
      searchEnginePanel = new javax.swing.JPanel();
      jSeparator4 = new javax.swing.JSeparator();
      jSeparator5 = new javax.swing.JSeparator();
      jPanel2 = new javax.swing.JPanel();
      jSeparator8 = new javax.swing.JSeparator();
      setSearchEnginePortLabel = new javax.swing.JLabel();
      updateSearhEnginePortNumberButton = new javax.swing.JButton();
      searchEnginePortModel = new SpinnerNumberModel(8888, 1, null, 1);
      searchEnginePortNumberSpinner = new javax.swing.JSpinner(searchEnginePortModel);
      jLabel1 = new javax.swing.JLabel();
      jPanel3 = new javax.swing.JPanel();
      siteAdminEMailTextField = new javax.swing.JTextField();
      organizationNameTextField = new javax.swing.JTextField();
      organizationURLTextField = new javax.swing.JTextField();
      setAdminEMailLabel = new javax.swing.JLabel();
      setOrganizationURLLabel = new javax.swing.JLabel();
      jSeparator9 = new javax.swing.JSeparator();
      setOrganizationNameLabel = new javax.swing.JLabel();
      updateAdminInfoButton = new javax.swing.JButton();
      jLabel2 = new javax.swing.JLabel();
      jPanel4 = new javax.swing.JPanel();
      searchEngineHelpLabel = new javax.swing.JLabel();
      launchSearchEngineLogFileLabel = new javax.swing.JLabel();
      jSeparator2 = new javax.swing.JSeparator();
      jPanel11 = new javax.swing.JPanel();
      jLabel4 = new javax.swing.JLabel();
      jLabel3 = new javax.swing.JLabel();
      jTextField1 = new javax.swing.JTextField();
      jTextField2 = new javax.swing.JTextField();
      jSeparator1 = new javax.swing.JSeparator();
      jLabel5 = new javax.swing.JLabel();
      jPanel1 = new javax.swing.JPanel();
      stopSearchEngineButton = new javax.swing.JButton();
      launchSearchEngineLabel = new javax.swing.JLabel();
      restartSearchEngineButton = new javax.swing.JButton();
      crawlerPanel = new javax.swing.JPanel();
      jPanel9 = new javax.swing.JPanel();
      crawlerHelpLabel = new javax.swing.JLabel();
      jPanel6 = new javax.swing.JPanel();
      jScrollPane1 = new javax.swing.JScrollPane();
      rootURLsListModel = new DefaultListModel();
      rootURLsList = new javax.swing.JList(rootURLsListModel);
      startingURLsLabel = new javax.swing.JLabel();
      removeURLButton = new javax.swing.JButton();
      addURLButton = new javax.swing.JButton();
      newURLTextField = new javax.swing.JTextField();
      jPanel7 = new javax.swing.JPanel();
      skipEmailLinksCheckBox = new javax.swing.JCheckBox();
      skipLocalFilesCheckBox = new javax.swing.JCheckBox();
      skipDynamicURLsCheckBox = new javax.swing.JCheckBox();
      advancedFilterSettingsButton = new javax.swing.JButton();
      skipFTPSitesCheckBox = new javax.swing.JCheckBox();
      filtersLabel = new javax.swing.JLabel();
      inclusionDomainLabel = new javax.swing.JLabel();
      inclusionDomainComboBox = new javax.swing.JComboBox();
      jSeparator10 = new javax.swing.JSeparator();
      skipProbableLoopsCheckBox = new javax.swing.JCheckBox();
      jPanel8 = new javax.swing.JPanel();
      configureCrawlerProxyButton = new javax.swing.JButton();
      setCrawlerCredentialsButton = new javax.swing.JButton();
      traversalDepthLabel = new javax.swing.JLabel();
      filtersLabel1 = new javax.swing.JLabel();
      crawlerDepthModel = new SpinnerNumberModel(5, 1, null, 1);
      traversalDepthSpinner = new javax.swing.JSpinner(crawlerDepthModel);
      jSeparator7 = new javax.swing.JSeparator();
      jPanel10 = new javax.swing.JPanel();
      crawlStatusLabel = new javax.swing.JLabel();
      stopCrawlButton = new javax.swing.JButton();
      restartSearchEngineCheckBox = new javax.swing.JCheckBox();
      initiateNewCrawlButton = new javax.swing.JButton();
      crawlStatusTextField = new javax.swing.JTextField();
      launchCrawlerLogFileLabel = new javax.swing.JLabel();
      viewCrawlerReportLabel = new javax.swing.JLabel();
      jSeparator3 = new javax.swing.JSeparator();
      menuBar = new javax.swing.JMenuBar();
      javax.swing.JMenu fileMenu = new javax.swing.JMenu();
      javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
      javax.swing.JMenu helpMenu = new javax.swing.JMenu();
      helpContentsMenuItem = new javax.swing.JMenuItem();
      javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
      forumMenuItem = new javax.swing.JMenuItem();
      donateMenuItem = new javax.swing.JMenuItem();

      mainPanel.setAutoscrolls(true);
      mainPanel.setDoubleBuffered(false);
      mainPanel.setName("mainPanel"); // NOI18N

      org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.whelanlabs.searchengine.AdminUI.class).getContext().getResourceMap(AdminUIView.class);
      jTabbedPane1.setFont(resourceMap.getFont("jTabbedPane1.font")); // NOI18N
      jTabbedPane1.setMinimumSize(new java.awt.Dimension(0, 0));
      jTabbedPane1.setName("jTabbedPane1"); // NOI18N

      searchEnginePanel.setName("searchEnginePanel"); // NOI18N

      jSeparator4.setName("jSeparator4"); // NOI18N

      jSeparator5.setName("jSeparator5"); // NOI18N

      jPanel2.setName("jPanel2"); // NOI18N

      jSeparator8.setName("jSeparator8"); // NOI18N

      setSearchEnginePortLabel.setForeground(resourceMap.getColor("setSearchEnginePortLabel.foreground")); // NOI18N
      setSearchEnginePortLabel.setText(resourceMap.getString("setSearchEnginePortLabel.text")); // NOI18N
      setSearchEnginePortLabel.setName("setSearchEnginePortLabel"); // NOI18N

      updateSearhEnginePortNumberButton.setText(resourceMap.getString("updateSearhEnginePortNumberButton.text")); // NOI18N
      updateSearhEnginePortNumberButton.setName("updateSearhEnginePortNumberButton"); // NOI18N
      updateSearhEnginePortNumberButton.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            updateSearhEnginePortNumberButtonMouseClicked(evt);
         }
      });

      searchEnginePortNumberSpinner.setName("searchEnginePortNumberSpinner"); // NOI18N

      jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
      jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
      jLabel1.setName("jLabel1"); // NOI18N

      javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
      jPanel2.setLayout(jPanel2Layout);
      jPanel2Layout.setHorizontalGroup(
         jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(jPanel2Layout.createSequentialGroup()
                  .addComponent(jSeparator8, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
                  .addContainerGap())
               .addGroup(jPanel2Layout.createSequentialGroup()
                  .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                     .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(setSearchEnginePortLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchEnginePortNumberSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(updateSearhEnginePortNumberButton))
                     .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                  .addGap(53, 53, 53))))
      );
      jPanel2Layout.setVerticalGroup(
         jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel2Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel1)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(searchEnginePortNumberSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(updateSearhEnginePortNumberButton)
               .addComponent(setSearchEnginePortLabel))
            .addContainerGap(18, Short.MAX_VALUE))
      );

      jPanel3.setName("jPanel3"); // NOI18N

      siteAdminEMailTextField.setText(resourceMap.getString("siteAdminEMailTextField.text")); // NOI18N
      siteAdminEMailTextField.setName("siteAdminEMailTextField"); // NOI18N

      organizationNameTextField.setText(resourceMap.getString("organizationNameTextField.text")); // NOI18N
      organizationNameTextField.setName("organizationNameTextField"); // NOI18N

      organizationURLTextField.setText(resourceMap.getString("organizationURLTextField.text")); // NOI18N
      organizationURLTextField.setName("organizationURLTextField"); // NOI18N

      setAdminEMailLabel.setForeground(resourceMap.getColor("setAdminEMailLabel.foreground")); // NOI18N
      setAdminEMailLabel.setText(resourceMap.getString("setAdminEMailLabel.text")); // NOI18N
      setAdminEMailLabel.setName("setAdminEMailLabel"); // NOI18N

      setOrganizationURLLabel.setForeground(resourceMap.getColor("setOrganizationURLLabel.foreground")); // NOI18N
      setOrganizationURLLabel.setText(resourceMap.getString("setOrganizationURLLabel.text")); // NOI18N
      setOrganizationURLLabel.setName("setOrganizationURLLabel"); // NOI18N

      jSeparator9.setName("jSeparator9"); // NOI18N

      setOrganizationNameLabel.setForeground(resourceMap.getColor("setOrganizationNameLabel.foreground")); // NOI18N
      setOrganizationNameLabel.setText(resourceMap.getString("setOrganizationNameLabel.text")); // NOI18N
      setOrganizationNameLabel.setName("setOrganizationNameLabel"); // NOI18N

      updateAdminInfoButton.setText(resourceMap.getString("updateAdminInfoButton.text")); // NOI18N
      updateAdminInfoButton.setName("updateAdminInfoButton"); // NOI18N
      updateAdminInfoButton.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            updateAdminInfoButtonMouseClicked(evt);
         }
      });

      jLabel2.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
      jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
      jLabel2.setName("jLabel2"); // NOI18N

      javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
      jPanel3.setLayout(jPanel3Layout);
      jPanel3Layout.setHorizontalGroup(
         jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel3Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(jSeparator9, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
               .addGroup(jPanel3Layout.createSequentialGroup()
                  .addGap(10, 10, 10)
                  .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                     .addComponent(setAdminEMailLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                     .addComponent(setOrganizationNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                     .addComponent(setOrganizationURLLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                  .addGap(10, 10, 10)
                  .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                     .addComponent(siteAdminEMailTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)
                     .addComponent(organizationURLTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)
                     .addComponent(updateAdminInfoButton)
                     .addComponent(organizationNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)))
               .addComponent(jLabel2))
            .addContainerGap())
      );
      jPanel3Layout.setVerticalGroup(
         jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel3Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel2)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(setOrganizationNameLabel)
               .addComponent(organizationNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(organizationURLTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(setAdminEMailLabel))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(siteAdminEMailTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(setOrganizationURLLabel))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(updateAdminInfoButton)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      jPanel4.setName("jPanel4"); // NOI18N

      searchEngineHelpLabel.setForeground(resourceMap.getColor("searchEngineHelpLabel.foreground")); // NOI18N
      searchEngineHelpLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
      searchEngineHelpLabel.setText(resourceMap.getString("searchEngineHelpLabel.text")); // NOI18N
      searchEngineHelpLabel.setName("searchEngineHelpLabel"); // NOI18N
      searchEngineHelpLabel.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            searchEngineHelpLabelMouseClicked(evt);
         }
         public void mouseEntered(java.awt.event.MouseEvent evt) {
            searchEngineHelpLabelMouseEntered(evt);
         }
      });

      launchSearchEngineLogFileLabel.setForeground(resourceMap.getColor("launchSearchEngineLogFileLabel.foreground")); // NOI18N
      launchSearchEngineLogFileLabel.setText(resourceMap.getString("launchSearchEngineLogFileLabel.text")); // NOI18N
      launchSearchEngineLogFileLabel.setName("launchSearchEngineLogFileLabel"); // NOI18N
      launchSearchEngineLogFileLabel.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            launchSearchEngineLogFileLabelMouseClicked(evt);
         }
         public void mouseEntered(java.awt.event.MouseEvent evt) {
            launchSearchEngineLogFileLabelMouseEntered(evt);
         }
      });

      jSeparator2.setName("jSeparator2"); // NOI18N

      javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
      jPanel4.setLayout(jPanel4Layout);
      jPanel4Layout.setHorizontalGroup(
         jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel4Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
               .addGroup(jPanel4Layout.createSequentialGroup()
                  .addComponent(launchSearchEngineLogFileLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 469, Short.MAX_VALUE)
                  .addComponent(searchEngineHelpLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap())
      );
      jPanel4Layout.setVerticalGroup(
         jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 144, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(launchSearchEngineLogFileLabel)
               .addComponent(searchEngineHelpLabel))
            .addContainerGap())
      );

      jPanel11.setName("jPanel11"); // NOI18N

      jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
      jLabel4.setName("jLabel4"); // NOI18N

      jLabel3.setFont(resourceMap.getFont("jLabel3.font")); // NOI18N
      jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
      jLabel3.setName("jLabel3"); // NOI18N

      jTextField1.setText(resourceMap.getString("jTextField1.text")); // NOI18N
      jTextField1.setName("jTextField1"); // NOI18N

      jTextField2.setText(resourceMap.getString("jTextField2.text")); // NOI18N
      jTextField2.setName("jTextField2"); // NOI18N

      jSeparator1.setName("jSeparator1"); // NOI18N

      jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
      jLabel5.setName("jLabel5"); // NOI18N

      javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
      jPanel11.setLayout(jPanel11Layout);
      jPanel11Layout.setHorizontalGroup(
         jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel11Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
               .addComponent(jLabel3)
               .addGroup(jPanel11Layout.createSequentialGroup()
                  .addGap(10, 10, 10)
                  .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                     .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                     .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE))
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                     .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE)
                     .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE))))
            .addContainerGap())
      );
      jPanel11Layout.setVerticalGroup(
         jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel11Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel3)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel4)
               .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(jLabel5)
               .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      jPanel1.setName("jPanel1"); // NOI18N

      stopSearchEngineButton.setText(resourceMap.getString("stopSearchEngineButton.text")); // NOI18N
      stopSearchEngineButton.setToolTipText(resourceMap.getString("stopSearchEngineButton.toolTipText")); // NOI18N
      stopSearchEngineButton.setName("stopSearchEngineButton"); // NOI18N
      stopSearchEngineButton.setPreferredSize(new java.awt.Dimension(180, 23));
      stopSearchEngineButton.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            stopSearchEngineButtonMouseClicked(evt);
         }
      });

      launchSearchEngineLabel.setFont(resourceMap.getFont("launchSearchEngineLabel.font")); // NOI18N
      launchSearchEngineLabel.setForeground(resourceMap.getColor("launchSearchEngineLabel.foreground")); // NOI18N
      launchSearchEngineLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
      launchSearchEngineLabel.setText(resourceMap.getString("launchSearchEngineLabel.text")); // NOI18N
      launchSearchEngineLabel.setName("launchSearchEngineLabel"); // NOI18N
      launchSearchEngineLabel.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            launchSearchEngineLabelMouseClicked(evt);
         }
         public void mouseEntered(java.awt.event.MouseEvent evt) {
            launchSearchEngineLabelMouseEntered(evt);
         }
      });

      restartSearchEngineButton.setText(resourceMap.getString("restartSearchEngineButton.text")); // NOI18N
      restartSearchEngineButton.setName("restartSearchEngineButton"); // NOI18N
      restartSearchEngineButton.setPreferredSize(new java.awt.Dimension(180, 23));
      restartSearchEngineButton.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            restartSearchEngineButtonMouseClicked(evt);
         }
      });

      javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
      jPanel1.setLayout(jPanel1Layout);
      jPanel1Layout.setHorizontalGroup(
         jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(restartSearchEngineButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(stopSearchEngineButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 241, Short.MAX_VALUE)
            .addComponent(launchSearchEngineLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
      );
      jPanel1Layout.setVerticalGroup(
         jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(restartSearchEngineButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(stopSearchEngineButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(launchSearchEngineLabel))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      javax.swing.GroupLayout searchEnginePanelLayout = new javax.swing.GroupLayout(searchEnginePanel);
      searchEnginePanel.setLayout(searchEnginePanelLayout);
      searchEnginePanelLayout.setHorizontalGroup(
         searchEnginePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, searchEnginePanelLayout.createSequentialGroup()
            .addGap(364, 364, 364)
            .addGroup(searchEnginePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(60, 60, 60))
         .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
         .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
         .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
         .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
         .addGroup(searchEnginePanelLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );
      searchEnginePanelLayout.setVerticalGroup(
         searchEnginePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(searchEnginePanelLayout.createSequentialGroup()
            .addGroup(searchEnginePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      jTabbedPane1.addTab(resourceMap.getString("searchEnginePanel.TabConstraints.tabTitle"), searchEnginePanel); // NOI18N

      crawlerPanel.setName("crawlerPanel"); // NOI18N

      jPanel9.setName("jPanel9"); // NOI18N

      crawlerHelpLabel.setForeground(resourceMap.getColor("crawlerHelpLabel.foreground")); // NOI18N
      crawlerHelpLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
      crawlerHelpLabel.setText(resourceMap.getString("crawlerHelpLabel.text")); // NOI18N
      crawlerHelpLabel.setName("crawlerHelpLabel"); // NOI18N
      crawlerHelpLabel.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            crawlerHelpLabelMouseClicked(evt);
         }
         public void mouseEntered(java.awt.event.MouseEvent evt) {
            crawlerHelpLabelMouseEntered(evt);
         }
      });

      javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
      jPanel9.setLayout(jPanel9Layout);
      jPanel9Layout.setHorizontalGroup(
         jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
            .addContainerGap(671, Short.MAX_VALUE)
            .addComponent(crawlerHelpLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
      );
      jPanel9Layout.setVerticalGroup(
         jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
            .addContainerGap(20, Short.MAX_VALUE)
            .addComponent(crawlerHelpLabel)
            .addContainerGap())
      );

      jPanel6.setName("jPanel6"); // NOI18N

      jScrollPane1.setAlignmentY(0.0F);
      jScrollPane1.setMinimumSize(new java.awt.Dimension(0, 0));
      jScrollPane1.setName("jScrollPane1"); // NOI18N
      jScrollPane1.setPreferredSize(new java.awt.Dimension(0, 0));

      rootURLsList.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
      rootURLsList.setMinimumSize(new java.awt.Dimension(50, 200));
      rootURLsList.setName("rootURLsList"); // NOI18N
      rootURLsList.setPreferredSize(new java.awt.Dimension(50, 50));
      jScrollPane1.setViewportView(rootURLsList);

      startingURLsLabel.setFont(resourceMap.getFont("startingURLsLabel.font")); // NOI18N
      startingURLsLabel.setForeground(resourceMap.getColor("filtersLabel.foreground")); // NOI18N
      startingURLsLabel.setText(resourceMap.getString("startingURLsLabel.text")); // NOI18N
      startingURLsLabel.setToolTipText(resourceMap.getString("startingURLsLabel.toolTipText")); // NOI18N
      startingURLsLabel.setName("startingURLsLabel"); // NOI18N
      startingURLsLabel.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseEntered(java.awt.event.MouseEvent evt) {
            startingURLsLabelMouseEntered(evt);
         }
      });

      removeURLButton.setText(resourceMap.getString("removeURLButton.text")); // NOI18N
      removeURLButton.setToolTipText(resourceMap.getString("removeURLButton.toolTipText")); // NOI18N
      removeURLButton.setName("removeURLButton"); // NOI18N
      removeURLButton.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            removeURLButtonMouseClicked(evt);
         }
      });

      addURLButton.setText(resourceMap.getString("addURLButton.text")); // NOI18N
      addURLButton.setToolTipText(resourceMap.getString("addURLButton.toolTipText")); // NOI18N
      addURLButton.setName("addURLButton"); // NOI18N
      addURLButton.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            addURLButtonMouseClicked(evt);
         }
      });

      newURLTextField.setText(resourceMap.getString("newURLTextField.text")); // NOI18N
      newURLTextField.setToolTipText(resourceMap.getString("newURLTextField.toolTipText")); // NOI18N
      newURLTextField.setMinimumSize(new java.awt.Dimension(0, 0));
      newURLTextField.setName("newURLTextField"); // NOI18N
      newURLTextField.setPreferredSize(new java.awt.Dimension(0, 0));
      newURLTextField.addFocusListener(new java.awt.event.FocusAdapter() {
         public void focusGained(java.awt.event.FocusEvent evt) {
            newURLTextFieldFocusGained(evt);
         }
         public void focusLost(java.awt.event.FocusEvent evt) {
            newURLTextFieldFocusLost(evt);
         }
      });

      javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
      jPanel6.setLayout(jPanel6Layout);
      jPanel6Layout.setHorizontalGroup(
         jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
               .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 781, Short.MAX_VALUE)
               .addComponent(newURLTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 781, Short.MAX_VALUE)
               .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                  .addComponent(addURLButton)
                  .addGap(18, 18, 18)
                  .addComponent(removeURLButton))
               .addComponent(startingURLsLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap())
      );
      jPanel6Layout.setVerticalGroup(
         jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel6Layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(startingURLsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(newURLTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(addURLButton)
               .addComponent(removeURLButton))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      jPanel7.setName("jPanel7"); // NOI18N

      skipEmailLinksCheckBox.setText(resourceMap.getString("skipEmailLinksCheckBox.text")); // NOI18N
      skipEmailLinksCheckBox.setToolTipText(resourceMap.getString("skipEmailLinksCheckBox.toolTipText")); // NOI18N
      skipEmailLinksCheckBox.setName("skipEmailLinksCheckBox"); // NOI18N
      skipEmailLinksCheckBox.setPreferredSize(new java.awt.Dimension(100, 23));
      skipEmailLinksCheckBox.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            skipEmailLinksCheckBoxActionPerformed(evt);
         }
      });

      skipLocalFilesCheckBox.setText(resourceMap.getString("skipLocalFilesCheckBox.text")); // NOI18N
      skipLocalFilesCheckBox.setToolTipText(resourceMap.getString("skipLocalFilesCheckBox.toolTipText")); // NOI18N
      skipLocalFilesCheckBox.setName("skipLocalFilesCheckBox"); // NOI18N
      skipLocalFilesCheckBox.setPreferredSize(new java.awt.Dimension(100, 23));
      skipLocalFilesCheckBox.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            skipLocalFilesCheckBoxActionPerformed(evt);
         }
      });

      skipDynamicURLsCheckBox.setText(resourceMap.getString("skipDynamicURLsCheckBox.text")); // NOI18N
      skipDynamicURLsCheckBox.setToolTipText(resourceMap.getString("skipDynamicURLsCheckBox.toolTipText")); // NOI18N
      skipDynamicURLsCheckBox.setName("skipDynamicURLsCheckBox"); // NOI18N
      skipDynamicURLsCheckBox.setPreferredSize(new java.awt.Dimension(100, 23));
      skipDynamicURLsCheckBox.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            skipDynamicURLsCheckBoxActionPerformed(evt);
         }
      });

      advancedFilterSettingsButton.setText(resourceMap.getString("advancedFilterSettingsButton.text")); // NOI18N
      advancedFilterSettingsButton.setName("advancedFilterSettingsButton"); // NOI18N
      advancedFilterSettingsButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            advancedFilterSettingsButtonActionPerformed(evt);
         }
      });

      skipFTPSitesCheckBox.setText(resourceMap.getString("skipFTPSitesCheckBox.text")); // NOI18N
      skipFTPSitesCheckBox.setToolTipText(resourceMap.getString("skipFTPSitesCheckBox.toolTipText")); // NOI18N
      skipFTPSitesCheckBox.setName("skipFTPSitesCheckBox"); // NOI18N
      skipFTPSitesCheckBox.setPreferredSize(new java.awt.Dimension(100, 23));
      skipFTPSitesCheckBox.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            skipFTPSitesCheckBoxActionPerformed(evt);
         }
      });

      filtersLabel.setFont(resourceMap.getFont("filtersLabel.font")); // NOI18N
      filtersLabel.setForeground(resourceMap.getColor("filtersLabel.foreground")); // NOI18N
      filtersLabel.setText(resourceMap.getString("filtersLabel.text")); // NOI18N
      filtersLabel.setToolTipText(resourceMap.getString("filtersLabel.toolTipText")); // NOI18N
      filtersLabel.setName("filtersLabel"); // NOI18N

      inclusionDomainLabel.setText(resourceMap.getString("inclusionDomainLabel.text")); // NOI18N
      inclusionDomainLabel.setName("inclusionDomainLabel"); // NOI18N

      inclusionDomainComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Include URLs with starting-point hosts", "Include URLs with starting-point domains", "Include URLs with starting-point context roots", "Include all Hosts" }));
      inclusionDomainComboBox.setName("inclusionDomainComboBox"); // NOI18N
      inclusionDomainComboBox.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            inclusionDomainComboBoxActionPerformed(evt);
         }
      });

      jSeparator10.setName("jSeparator10"); // NOI18N

      skipProbableLoopsCheckBox.setText(resourceMap.getString("skipProbableLoopsCheckBox.text")); // NOI18N
      skipProbableLoopsCheckBox.setToolTipText(resourceMap.getString("skipProbableLoopsCheckBox.toolTipText")); // NOI18N
      skipProbableLoopsCheckBox.setName("skipProbableLoopsCheckBox"); // NOI18N
      skipProbableLoopsCheckBox.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            skipProbableLoopsCheckBoxActionPerformed(evt);
         }
      });

      javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
      jPanel7.setLayout(jPanel7Layout);
      jPanel7Layout.setHorizontalGroup(
         jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel7Layout.createSequentialGroup()
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(jPanel7Layout.createSequentialGroup()
                  .addContainerGap()
                  .addComponent(filtersLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
               .addGroup(jPanel7Layout.createSequentialGroup()
                  .addGap(20, 20, 20)
                  .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                     .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(skipLocalFilesCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(skipDynamicURLsCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(skipFTPSitesCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(skipEmailLinksCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(skipProbableLoopsCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                     .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(inclusionDomainLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(inclusionDomainComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(advancedFilterSettingsButton)))
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
               .addGroup(jPanel7Layout.createSequentialGroup()
                  .addContainerGap()
                  .addComponent(jSeparator10, javax.swing.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE)))
            .addContainerGap())
      );
      jPanel7Layout.setVerticalGroup(
         jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel7Layout.createSequentialGroup()
            .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(filtersLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(skipFTPSitesCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(skipEmailLinksCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(skipLocalFilesCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(skipDynamicURLsCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(skipProbableLoopsCheckBox))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(inclusionDomainLabel)
               .addComponent(inclusionDomainComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(advancedFilterSettingsButton))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      jPanel8.setName("jPanel8"); // NOI18N

      configureCrawlerProxyButton.setText(resourceMap.getString("configureCrawlerProxyButton.text")); // NOI18N
      configureCrawlerProxyButton.setToolTipText(resourceMap.getString("configureCrawlerProxyButton.toolTipText")); // NOI18N
      configureCrawlerProxyButton.setName("configureCrawlerProxyButton"); // NOI18N
      configureCrawlerProxyButton.setPreferredSize(new java.awt.Dimension(200, 23));
      configureCrawlerProxyButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            configureCrawlerProxyButtonActionPerformed(evt);
         }
      });

      setCrawlerCredentialsButton.setText(resourceMap.getString("setCrawlerCredentialsButton.text")); // NOI18N
      setCrawlerCredentialsButton.setToolTipText(resourceMap.getString("setCrawlerCredentialsButton.toolTipText")); // NOI18N
      setCrawlerCredentialsButton.setName("setCrawlerCredentialsButton"); // NOI18N
      setCrawlerCredentialsButton.setPreferredSize(new java.awt.Dimension(200, 23));
      setCrawlerCredentialsButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            setCrawlerCredentialsButtonActionPerformed(evt);
         }
      });

      traversalDepthLabel.setForeground(resourceMap.getColor("traversalDepthLabel.foreground")); // NOI18N
      traversalDepthLabel.setText(resourceMap.getString("traversalDepthLabel.text")); // NOI18N
      traversalDepthLabel.setToolTipText(resourceMap.getString("traversalDepthLabel.toolTipText")); // NOI18N
      traversalDepthLabel.setName("traversalDepthLabel"); // NOI18N

      filtersLabel1.setFont(resourceMap.getFont("filtersLabel1.font")); // NOI18N
      filtersLabel1.setText(resourceMap.getString("filtersLabel1.text")); // NOI18N
      filtersLabel1.setToolTipText(resourceMap.getString("filtersLabel1.toolTipText")); // NOI18N
      filtersLabel1.setName("filtersLabel1"); // NOI18N

      traversalDepthSpinner.setMinimumSize(new java.awt.Dimension(100, 18));
      traversalDepthSpinner.setName("traversalDepthSpinner"); // NOI18N
      traversalDepthSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
         public void stateChanged(javax.swing.event.ChangeEvent evt) {
            traversalDepthSpinnerStateChanged(evt);
         }
      });

      jSeparator7.setName("jSeparator7"); // NOI18N

      javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
      jPanel8.setLayout(jPanel8Layout);
      jPanel8Layout.setHorizontalGroup(
         jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel8Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(jPanel8Layout.createSequentialGroup()
                  .addComponent(jSeparator7, javax.swing.GroupLayout.DEFAULT_SIZE, 771, Short.MAX_VALUE)
                  .addContainerGap())
               .addGroup(jPanel8Layout.createSequentialGroup()
                  .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                     .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                           .addGroup(jPanel8Layout.createSequentialGroup()
                              .addComponent(setCrawlerCredentialsButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                              .addComponent(configureCrawlerProxyButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                           .addGroup(jPanel8Layout.createSequentialGroup()
                              .addComponent(traversalDepthLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                              .addComponent(traversalDepthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))))
                     .addComponent(filtersLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                  .addGap(361, 361, 361))))
      );
      jPanel8Layout.setVerticalGroup(
         jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel8Layout.createSequentialGroup()
            .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(1, 1, 1)
            .addComponent(filtersLabel1)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(traversalDepthSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(traversalDepthLabel))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(setCrawlerCredentialsButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(configureCrawlerProxyButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      jPanel10.setName("jPanel10"); // NOI18N

      crawlStatusLabel.setFont(resourceMap.getFont("crawlStatusLabel.font")); // NOI18N
      crawlStatusLabel.setForeground(resourceMap.getColor("crawlStatusLabel.foreground")); // NOI18N
      crawlStatusLabel.setText(resourceMap.getString("crawlStatusLabel.text")); // NOI18N
      crawlStatusLabel.setToolTipText(resourceMap.getString("crawlStatusLabel.toolTipText")); // NOI18N
      crawlStatusLabel.setName("crawlStatusLabel"); // NOI18N

      stopCrawlButton.setText(resourceMap.getString("stopCrawlButton.text")); // NOI18N
      stopCrawlButton.setEnabled(false);
      stopCrawlButton.setName("stopCrawlButton"); // NOI18N
      stopCrawlButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            stopCrawlButtonActionPerformed(evt);
         }
      });

      restartSearchEngineCheckBox.setText(resourceMap.getString("restartSearchEngineCheckBox.text")); // NOI18N
      restartSearchEngineCheckBox.setName("restartSearchEngineCheckBox"); // NOI18N

      initiateNewCrawlButton.setText(resourceMap.getString("initiateNewCrawlButton.text")); // NOI18N
      initiateNewCrawlButton.setToolTipText(resourceMap.getString("initiateNewCrawlButton.toolTipText")); // NOI18N
      initiateNewCrawlButton.setName("initiateNewCrawlButton"); // NOI18N
      initiateNewCrawlButton.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            initiateNewCrawlButtonMouseClicked(evt);
         }
      });

      crawlStatusTextField.setEditable(false);
      crawlStatusTextField.setText(resourceMap.getString("crawlStatusTextField.text")); // NOI18N
      crawlStatusTextField.setName("crawlStatusTextField"); // NOI18N

      launchCrawlerLogFileLabel.setForeground(resourceMap.getColor("launchCrawlerLogFileLabel.foreground")); // NOI18N
      launchCrawlerLogFileLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
      launchCrawlerLogFileLabel.setText(resourceMap.getString("launchCrawlerLogFileLabel.text")); // NOI18N
      launchCrawlerLogFileLabel.setName("launchCrawlerLogFileLabel"); // NOI18N
      launchCrawlerLogFileLabel.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            launchCrawlerLogFileLabelMouseClicked(evt);
         }
         public void mouseEntered(java.awt.event.MouseEvent evt) {
            launchCrawlerLogFileLabelMouseEntered(evt);
         }
      });

      viewCrawlerReportLabel.setForeground(resourceMap.getColor("viewCrawlerReportLabel.foreground")); // NOI18N
      viewCrawlerReportLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
      viewCrawlerReportLabel.setText(resourceMap.getString("viewCrawlerReportLabel.text")); // NOI18N
      viewCrawlerReportLabel.setName("viewCrawlerReportLabel"); // NOI18N
      viewCrawlerReportLabel.addMouseListener(new java.awt.event.MouseAdapter() {
         public void mouseClicked(java.awt.event.MouseEvent evt) {
            viewCrawlerReportLabelMouseClicked(evt);
         }
         public void mouseEntered(java.awt.event.MouseEvent evt) {
            viewCrawlerReportLabelMouseEntered(evt);
         }
      });

      jSeparator3.setName("jSeparator3"); // NOI18N

      javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
      jPanel10.setLayout(jPanel10Layout);
      jPanel10Layout.setHorizontalGroup(
         jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
               .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 781, Short.MAX_VALUE)
               .addGroup(jPanel10Layout.createSequentialGroup()
                  .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                     .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(crawlStatusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(crawlStatusTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(initiateNewCrawlButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stopCrawlButton))
                     .addComponent(restartSearchEngineCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE))
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 129, Short.MAX_VALUE)
                  .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                     .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(launchCrawlerLogFileLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                     .addComponent(viewCrawlerReportLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addContainerGap())
      );
      jPanel10Layout.setVerticalGroup(
         jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(jPanel10Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(jPanel10Layout.createSequentialGroup()
                  .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                     .addComponent(crawlStatusTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                     .addComponent(initiateNewCrawlButton)
                     .addComponent(stopCrawlButton)
                     .addComponent(crawlStatusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(restartSearchEngineCheckBox))
               .addGroup(jPanel10Layout.createSequentialGroup()
                  .addComponent(viewCrawlerReportLabel)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(launchCrawlerLogFileLabel)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      javax.swing.GroupLayout crawlerPanelLayout = new javax.swing.GroupLayout(crawlerPanel);
      crawlerPanel.setLayout(crawlerPanelLayout);
      crawlerPanelLayout.setHorizontalGroup(
         crawlerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
         .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
         .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
         .addGroup(crawlerPanelLayout.createSequentialGroup()
            .addGap(10, 10, 10)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
         .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      );
      crawlerPanelLayout.setVerticalGroup(
         crawlerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(crawlerPanelLayout.createSequentialGroup()
            .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );

      jTabbedPane1.addTab(resourceMap.getString("crawlerPanel.TabConstraints.tabTitle"), crawlerPanel); // NOI18N

      javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
      mainPanel.setLayout(mainPanelLayout);
      mainPanelLayout.setHorizontalGroup(
         mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 805, Short.MAX_VALUE)
      );
      mainPanelLayout.setVerticalGroup(
         mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 641, Short.MAX_VALUE)
      );

      menuBar.setName("menuBar"); // NOI18N

      fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
      fileMenu.setName("fileMenu"); // NOI18N

      javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.whelanlabs.searchengine.AdminUI.class).getContext().getActionMap(AdminUIView.class, this);
      exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
      exitMenuItem.setName("exitMenuItem"); // NOI18N
      fileMenu.add(exitMenuItem);

      menuBar.add(fileMenu);

      helpMenu.setAction(actionMap.get("ShowDonation")); // NOI18N
      helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
      helpMenu.setName("helpMenu"); // NOI18N

      helpContentsMenuItem.setAction(actionMap.get("ShowHelp")); // NOI18N
      helpContentsMenuItem.setText(resourceMap.getString("helpContentsMenuItem.text")); // NOI18N
      helpContentsMenuItem.setName("helpContentsMenuItem"); // NOI18N
      helpMenu.add(helpContentsMenuItem);

      aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
      aboutMenuItem.setName("aboutMenuItem"); // NOI18N
      helpMenu.add(aboutMenuItem);

      forumMenuItem.setAction(actionMap.get("ShowOnlineForum")); // NOI18N
      forumMenuItem.setText(resourceMap.getString("forumMenuItem.text")); // NOI18N
      forumMenuItem.setName("forumMenuItem"); // NOI18N
      helpMenu.add(forumMenuItem);

      donateMenuItem.setAction(actionMap.get("ShowDonation")); // NOI18N
      donateMenuItem.setText(resourceMap.getString("donateMenuItem.text")); // NOI18N
      donateMenuItem.setName("donateMenuItem"); // NOI18N
      helpMenu.add(donateMenuItem);

      menuBar.add(helpMenu);

      setComponent(mainPanel);
      setMenuBar(menuBar);
   }// </editor-fold>//GEN-END:initComponents

private void launchSearchEngineLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_launchSearchEngineLabelMouseClicked
      try {
         String engineURL = "http://localhost:" + properties.getProperty("searchEnginePortNumber") + "/";
         URI uri = new URI(engineURL);
         java.awt.Desktop desktop = Desktop.getDesktop();
         desktop.browse(uri);
      } catch (IOException ex) {
         Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, ex);
      } catch (URISyntaxException ex) {
         Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, ex);
      }
}//GEN-LAST:event_launchSearchEngineLabelMouseClicked

private void launchSearchEngineLogFileLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_launchSearchEngineLogFileLabelMouseClicked
   try {
      String url = appRootDirectory + "/cygwin/whelanlabs/searchengine/tomcat/logs/catalina.out";
      File f = new File(url);
      if (f.exists()) {
         URI uri = new URI(url);
         java.awt.Desktop desktop = Desktop.getDesktop();
         desktop.browse(uri);
      } else {
         JOptionPane.showMessageDialog(mainPanel, "The search engine log file does not exist.", "File not found", JOptionPane.INFORMATION_MESSAGE);
      }
   } catch (IOException ex) {
      Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, ex);
   } catch (URISyntaxException ex) {
      Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, ex);
   }
}//GEN-LAST:event_launchSearchEngineLogFileLabelMouseClicked

private void launchCrawlerLogFileLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_launchCrawlerLogFileLabelMouseClicked
   try {
      String url = appRootDirectory + "/cygwin/whelanlabs/searchengine/crawl.log";
      File f = new File(url);
      if (f.exists()) {
         URI uri = new URI(url);
         java.awt.Desktop desktop = Desktop.getDesktop();
         desktop.browse(uri);
      } else {
         JOptionPane.showMessageDialog(mainPanel, "The crawler log file does not exist.", "File not found", JOptionPane.INFORMATION_MESSAGE);
      }
   } catch (IOException ex) {
      Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, ex);
   } catch (URISyntaxException ex) {
      Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, ex);
   }
}//GEN-LAST:event_launchCrawlerLogFileLabelMouseClicked

private void initiateNewCrawlButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_initiateNewCrawlButtonMouseClicked
    
    try {
      updateCrawlStatusTextField("Crawl in Progress");
      String filename = "\"" + cygwinDirectory + "/bin/bash.exe\" --login /whelanlabs/searchengine/reindexNutch.sh";
      Process p = Runtime.getRuntime().exec(filename);
      p.waitFor();
    }
    catch (Exception err) {
       Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, err);
    }
}//GEN-LAST:event_initiateNewCrawlButtonMouseClicked

private void launchSearchEngineLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_launchSearchEngineLabelMouseEntered
   Cursor hourglassCursor = new Cursor(Cursor.HAND_CURSOR);
   evt.getComponent().setCursor(hourglassCursor);
}//GEN-LAST:event_launchSearchEngineLabelMouseEntered

private void launchSearchEngineLogFileLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_launchSearchEngineLogFileLabelMouseEntered
   Cursor hourglassCursor = new Cursor(Cursor.HAND_CURSOR);
   evt.getComponent().setCursor(hourglassCursor);
}//GEN-LAST:event_launchSearchEngineLogFileLabelMouseEntered

private void launchCrawlerLogFileLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_launchCrawlerLogFileLabelMouseEntered
   Cursor hourglassCursor = new Cursor(Cursor.HAND_CURSOR);
   evt.getComponent().setCursor(hourglassCursor);
}//GEN-LAST:event_launchCrawlerLogFileLabelMouseEntered

private void addURLButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addURLButtonMouseClicked
   if (newURLTextField.getText().contains("://")) {
      int pos = rootURLsList.getModel().getSize();
      rootURLsListModel.add(pos, newURLTextField.getText());
      updateURLs();
   } else {
      JFrame mainFrame = AdminUI.getApplication().getMainFrame();
      JOptionPane.showMessageDialog(mainFrame,
              "Invalid URL.",
              "WhelanLabs Search Engine",
              JOptionPane.ERROR_MESSAGE);
   }
}//GEN-LAST:event_addURLButtonMouseClicked

private void removeURLButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_removeURLButtonMouseClicked
      int[] selected = rootURLsList.getSelectedIndices();
      int size = Array.getLength(selected);
      for(int i=size-1; i>=0; i--) {
         rootURLsListModel.remove(selected[i]);
      }
      updateURLs();
}//GEN-LAST:event_removeURLButtonMouseClicked

private void restartSearchEngineButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_restartSearchEngineButtonMouseClicked
   Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
   Cursor normalCursor = evt.getComponent().getCursor();
   evt.getComponent().setCursor(hourglassCursor);
   try {
         restartEngine();
    }
    catch (Exception err) {
       Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, err);
    }
   evt.getComponent().setCursor(normalCursor);
}//GEN-LAST:event_restartSearchEngineButtonMouseClicked

private void updateSearhEnginePortNumberButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_updateSearhEnginePortNumberButtonMouseClicked
   String newPort = searchEnginePortModel.getValue().toString();
   String templateFileName = appRootDirectory + "/config/server.xml.template";
   String outputFileName = appRootDirectory + "/cygwin/whelanlabs/searchengine/tomcat/conf/server.xml";
   generateFile(templateFileName, outputFileName, "@whelanlabs_searchengine_port@", newPort);
   properties.setProperty("searchEnginePortNumber", newPort);
   try {
      String filename = appRootDirectory + "/config/adminUI.properties";
      properties.store(new FileOutputStream(filename), null);
   } catch (IOException ex) {
      Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, ex);
   }
}//GEN-LAST:event_updateSearhEnginePortNumberButtonMouseClicked

private void stopSearchEngineButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stopSearchEngineButtonMouseClicked
   Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
   Cursor normalCursor = evt.getComponent().getCursor();
   evt.getComponent().setCursor(hourglassCursor);
   stopSearchEngine();
   evt.getComponent().setCursor(normalCursor);
}//GEN-LAST:event_stopSearchEngineButtonMouseClicked

private void updateAdminInfoButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_updateAdminInfoButtonMouseClicked

   properties.setProperty("organizationName", organizationNameTextField.getText());
   properties.setProperty("organizationURL", organizationURLTextField.getText());
   properties.setProperty("siteAdminEMail", siteAdminEMailTextField.getText());

   try {
      String filename = appRootDirectory + "/config/adminUI.properties";
      properties.store(new FileOutputStream(filename), null);
   } catch (IOException ex) {
      Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, ex);
   }
   
   TemplateUtils.updateNutchConfig(appRootDirectory);
   
}//GEN-LAST:event_updateAdminInfoButtonMouseClicked

private void crawlerHelpLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_crawlerHelpLabelMouseClicked
      try {
         String url = appRootDirectory + "/helpText/CrawlerHelp.htm";
         URI uri = new URI(url);
         java.awt.Desktop desktop = Desktop.getDesktop();
         desktop.browse(uri);
      } catch (IOException ex) {
         Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, ex);
      } catch (URISyntaxException ex) {
         Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, ex);
      }
}//GEN-LAST:event_crawlerHelpLabelMouseClicked

private void crawlerHelpLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_crawlerHelpLabelMouseEntered
   Cursor hourglassCursor = new Cursor(Cursor.HAND_CURSOR);
   evt.getComponent().setCursor(hourglassCursor);
}//GEN-LAST:event_crawlerHelpLabelMouseEntered

private void startingURLsLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startingURLsLabelMouseEntered
   Cursor hourglassCursor = new Cursor(Cursor.HAND_CURSOR);
   evt.getComponent().setCursor(hourglassCursor);
}//GEN-LAST:event_startingURLsLabelMouseEntered

private void searchEngineHelpLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchEngineHelpLabelMouseEntered
   Cursor hourglassCursor = new Cursor(Cursor.HAND_CURSOR);
   evt.getComponent().setCursor(hourglassCursor);
}//GEN-LAST:event_searchEngineHelpLabelMouseEntered

private void searchEngineHelpLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchEngineHelpLabelMouseClicked
      try {
         String url = appRootDirectory + "/helpText/SearchEngineHelp.htm";
         URI uri = new URI(url);
         java.awt.Desktop desktop = Desktop.getDesktop();
         desktop.browse(uri);
      } catch (IOException ex) {
         Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, ex);
      } catch (URISyntaxException ex) {
         Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, ex);
      }
}//GEN-LAST:event_searchEngineHelpLabelMouseClicked

private void skipLocalFilesCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_skipLocalFilesCheckBoxActionPerformed
   rewriteUrlFilter();
}//GEN-LAST:event_skipLocalFilesCheckBoxActionPerformed

private void skipFTPSitesCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_skipFTPSitesCheckBoxActionPerformed
   rewriteUrlFilter();
}//GEN-LAST:event_skipFTPSitesCheckBoxActionPerformed

private void skipEmailLinksCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_skipEmailLinksCheckBoxActionPerformed
   rewriteUrlFilter();
}//GEN-LAST:event_skipEmailLinksCheckBoxActionPerformed

private void skipDynamicURLsCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_skipDynamicURLsCheckBoxActionPerformed
   rewriteUrlFilter();
}//GEN-LAST:event_skipDynamicURLsCheckBoxActionPerformed

private void inclusionDomainComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inclusionDomainComboBoxActionPerformed
   rewriteUrlFilter();
}//GEN-LAST:event_inclusionDomainComboBoxActionPerformed

private void viewCrawlerReportLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewCrawlerReportLabelMouseEntered
   Cursor hourglassCursor = new Cursor(Cursor.HAND_CURSOR);
   evt.getComponent().setCursor(hourglassCursor);
}//GEN-LAST:event_viewCrawlerReportLabelMouseEntered

private void viewCrawlerReportLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewCrawlerReportLabelMouseClicked
   try {
      String url = appRootDirectory + "/cygwin/whelanlabs/searchengine/crawlerReport.html";
      String crawlerReport = CrawlerReport.generateReport(appRootDirectory);
      TemplateUtils.setFileText(crawlerReport, url);
      File f = new File(url);
      if (f.exists()) {
         URI uri = new URI(url);
         java.awt.Desktop desktop = Desktop.getDesktop();
         desktop.browse(uri);
      } else {
         JOptionPane.showMessageDialog(mainPanel, "The crawler log file does not exist.", "File not found", JOptionPane.INFORMATION_MESSAGE);
      }
   } catch (IOException ex) {
      Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, ex);
   } catch (URISyntaxException ex) {
      Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, ex);
   }
}//GEN-LAST:event_viewCrawlerReportLabelMouseClicked

private void traversalDepthSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_traversalDepthSpinnerStateChanged
properties.setProperty("crawlerDepth", crawlerDepthModel.getValue().toString());
   try {
      String adminUIFilename = appRootDirectory + "/config/adminUI.properties";
      properties.store(new FileOutputStream(adminUIFilename), null);
      
      Properties unixProps = new Properties();
      unixProps.setProperty("crawlerDepth", crawlerDepthModel.getValue().toString());
      String unixFilename = appRootDirectory + "/cygwin/whelanlabs/searchengine/depth.sh";
      unixProps.store(new FileOutputStream(unixFilename), null);
      
   } catch (IOException ex) {
      Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, ex);
   }
}//GEN-LAST:event_traversalDepthSpinnerStateChanged

private void stopCrawlButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopCrawlButtonActionPerformed
   try {
       // TODO: set cursor to hourglass?
      String filename = "\"" + cygwinDirectory + "/bin/bash.exe\" --login /whelanlabs/searchengine/stopCrawl.sh";
      Process p = Runtime.getRuntime().exec(filename);
      p.waitFor();
    }
    catch (Exception err) {
       Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, err);
    }
}//GEN-LAST:event_stopCrawlButtonActionPerformed

private void configureCrawlerProxyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_configureCrawlerProxyButtonActionPerformed
   ProxyConfigurationDialog proxyDialog;
   JFrame mainFrame = AdminUI.getApplication().getMainFrame();
   proxyDialog = new ProxyConfigurationDialog(mainFrame, true, appRootDirectory);
   proxyDialog.setLocationRelativeTo(mainFrame);
   AdminUI.getApplication().show(proxyDialog);
}//GEN-LAST:event_configureCrawlerProxyButtonActionPerformed

private void setCrawlerCredentialsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setCrawlerCredentialsButtonActionPerformed
   CrawlerCredentialsDialog credentialsDialog;
   JFrame mainFrame = AdminUI.getApplication().getMainFrame();
   credentialsDialog = new CrawlerCredentialsDialog(mainFrame, true, appRootDirectory);
   credentialsDialog.setLocationRelativeTo(mainFrame);
   AdminUI.getApplication().show(credentialsDialog);
}//GEN-LAST:event_setCrawlerCredentialsButtonActionPerformed

private void advancedFilterSettingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_advancedFilterSettingsButtonActionPerformed
// TODO add your handling code here:
   AdvancedFiltersDialog filtersDialog;
   JFrame mainFrame = AdminUI.getApplication().getMainFrame();
   filtersDialog = new AdvancedFiltersDialog(mainFrame, true, appRootDirectory);
   filtersDialog.setLocationRelativeTo(mainFrame);
   AdminUI.getApplication().show(filtersDialog);
}//GEN-LAST:event_advancedFilterSettingsButtonActionPerformed

private void skipProbableLoopsCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_skipProbableLoopsCheckBoxActionPerformed
   rewriteUrlFilter();
}//GEN-LAST:event_skipProbableLoopsCheckBoxActionPerformed

private void newURLTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_newURLTextFieldFocusGained
   getRootPane().setDefaultButton(addURLButton);
}//GEN-LAST:event_newURLTextFieldFocusGained

private void newURLTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_newURLTextFieldFocusLost
   getRootPane().setDefaultButton(null);
}//GEN-LAST:event_newURLTextFieldFocusLost

 
private void stopSearchEngine() {
   try {
       // TODO: set cursor to hourglass?
      String filename = "\"" + cygwinDirectory + "/bin/bash.exe\" --login /whelanlabs/searchengine/stopNutch.sh";
      Process p = Runtime.getRuntime().exec(filename);
      p.waitFor();
    }
    catch (Exception err) {
       Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, err);
    }
}

   /** This method is called from within the constructor to
    * load the existing configuration values.
    */
   private void loadConfiguration() {
      FileInputStream fstream = null;

      // hide non-implemented features:
      jSeparator2.setVisible(false);
      jTextField1.setVisible(false);
      jTextField2.setVisible(false);
      jLabel3.setVisible(false);
      jLabel4.setVisible(false);
      jLabel5.setVisible(false);
      
      appRootDirectory = System.getProperty("appRootDirectory");
      appRootDirectory = appRootDirectory.replace('\\', '/');
      
      cygwinDirectory = System.getProperty("cygwinDirectory");
      cygwinDirectory = cygwinDirectory.replace('\\', '/');
      
      if (null == appRootDirectory) {
         String errorMsg = "Configuration Error: The System Property 'appRootDirectory' has not been set.";
         Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, errorMsg);
         JFrame mainFrame = AdminUI.getApplication().getMainFrame();
         JOptionPane.showMessageDialog(mainFrame,
                 errorMsg,
                 "WhelanLabs Search Engine",
                 JOptionPane.ERROR_MESSAGE);
         System.exit(-1);
      }
      
      if (null == cygwinDirectory) {
         String errorMsg = "Configuration Error: The System Property 'cygwinDirectory' has not been set.";
         Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, errorMsg);
         JFrame mainFrame = AdminUI.getApplication().getMainFrame();
         JOptionPane.showMessageDialog(mainFrame,
                 errorMsg,
                 "WhelanLabs Search Engine",
                 JOptionPane.ERROR_MESSAGE);
         // TODO: add logging message
         System.exit(-1);
      }
      
      try {
         String filename = "\"" + cygwinDirectory + "/bin/bash.exe\" --login /whelanlabs/searchengine/cleanOnStartup.sh";
         Process p = Runtime.getRuntime().exec(filename);
         p.waitFor();
      } catch (Exception err) {
         Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, err);
      }

      properties = new Properties();
      try {
         String filename = appRootDirectory + "/config/adminUI.properties";
         properties.load(new FileInputStream(filename));
      } catch (IOException ex) {
         Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, ex);
      }
      
      Integer crawlerDepthInteger = new Integer(properties.getProperty("crawlerDepth"));
      crawlerDepthModel.setValue(crawlerDepthInteger);
      Integer searchEnginePortInteger = new Integer(properties.getProperty("searchEnginePortNumber"));
      searchEnginePortModel.setValue(searchEnginePortInteger);
      
      organizationNameTextField.setText(properties.getProperty("organizationName"));
      organizationURLTextField.setText(properties.getProperty("organizationURL"));
      siteAdminEMailTextField.setText(properties.getProperty("siteAdminEMail"));

      try {
         String rootURLsFile = appRootDirectory + "/cygwin/whelanlabs/searchengine/urls/urls.txt";
         fstream = new FileInputStream(rootURLsFile);
         DataInputStream in = new DataInputStream(fstream);

         while (in.available() != 0) {
            int posRootURLs = rootURLsList.getModel().getSize();
            rootURLsListModel.add(posRootURLs, in.readLine());
         }
         in.close();

      } catch (FileNotFoundException ex) {
         Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, ex);
      } catch (IOException ex) {
         Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
         try {
            fstream.close();
         } catch (IOException ex) {
            Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
      
      
      // properties.setProperty("CrawlerSkipFTPSites", "false");
      skipFTPSitesCheckBox.setSelected(new Boolean(properties.getProperty("CrawlerSkipFTPSites")));
      
      // properties.setProperty("CrawlerSkipLocalFiles", "false");
      skipLocalFilesCheckBox.setSelected(new Boolean(properties.getProperty("CrawlerSkipLocalFiles")));
      
      // properties.setProperty("CrawlerSkipDynamicURLs", "false");
      skipDynamicURLsCheckBox.setSelected(new Boolean(properties.getProperty("CrawlerSkipDynamicURLs")));
      
      // properties.setProperty("CrawlerSkipEmailLinks", "false");
      skipEmailLinksCheckBox.setSelected(new Boolean(properties.getProperty("CrawlerSkipEmailLinks")));
      
      // properties.setProperty("CrawlerInclusionLevel", #...
      try {
      inclusionDomainComboBox.setSelectedIndex(Integer.valueOf(properties.getProperty("CrawlerInclusionLevel")));
      }
      catch (NumberFormatException nfe)
      {
         inclusionDomainComboBox.setSelectedIndex(0);
      }

      // TODO: launch thread to check crawling status.
      CrawlerMonitor cm = new CrawlerMonitor(this, appRootDirectory);
      Thread crawlMonmitorThread = new Thread(cm);
      crawlMonmitorThread.start();

      // jTabbedPane1.set
   }
   
   private void updateURLs() {
      String filename = appRootDirectory + "/cygwin/whelanlabs/searchengine/urls/urls.txt";
      ListModel model = rootURLsList.getModel();
      writeToFile(filename, model);
      rewriteUrlFilter();
   }
   
   
   protected void updateCrawlStatusTextField(String newValue) {
      crawlStatusTextField.setText(newValue);
      if ("Crawl in Progress".equals(newValue)) {
         initiateNewCrawlButton.setEnabled(false);
         restartSearchEngineCheckBox.setEnabled(false);
         stopCrawlButton.setEnabled(true);
      } else if ("New Crawl Available".equals(newValue)) {
         if (!initiateNewCrawlButton.isEnabled()) {
            try {
               if (restartSearchEngineCheckBox.isSelected()) {
                  restartEngine();
               }
            } catch (Exception err) {
               Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, err);
            } finally {
               restartSearchEngineCheckBox.setEnabled(true);
               initiateNewCrawlButton.setEnabled(true);
               stopCrawlButton.setEnabled(false);
            }
         }
      } else {
         initiateNewCrawlButton.setEnabled(true);
         stopCrawlButton.setEnabled(false);
         restartSearchEngineCheckBox.setEnabled(true);
      }
   }
           
   private void writeToFile(String filename, ListModel model) {
      BufferedWriter out = null;
      try {
         out = new BufferedWriter(new FileWriter(filename));
         int modelSize = model.getSize();
         for (int i = 0; i < modelSize; i++) {
            out.write((String) model.getElementAt(i) + "\n");
         }

      } catch (IOException ex) {
         Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
         try {
            out.close();
         } catch (IOException ex) {
            Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
   }

   @Action
   public void ShowHelp() {
      try {
         String url = appRootDirectory + "/helpText/MainHelp.htm";
         URI uri = new URI(url);
         java.awt.Desktop desktop = Desktop.getDesktop();
         desktop.browse(uri);
      } catch (IOException ex) {
         Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, ex);
      } catch (URISyntaxException ex) {
         Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   @Action
   public void ShowOnlineForum() {
      try {
         String url = "http://n2.nabble.com/WhelanLabs-SearchEngine-Manager-f1641671.html";
         URI uri = new URI(url);
         java.awt.Desktop desktop = Desktop.getDesktop();
         desktop.browse(uri);
      } catch (IOException ex) {
         Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, ex);
      } catch (URISyntaxException ex) {
         Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   @Action
   public void ShowDonation() {
      try {
         String url = "https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=F6W3ERRW6G3Z2&lc=US&item_name=WhelanLabs&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted";
         URI uri = new URI(url);
         java.awt.Desktop desktop = Desktop.getDesktop();
         desktop.browse(uri);
      } catch (IOException ex) {
         Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, ex);
      } catch (URISyntaxException ex) {
         Logger.getLogger(AdminUIView.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JButton addURLButton;
   private javax.swing.JButton advancedFilterSettingsButton;
   private javax.swing.JButton configureCrawlerProxyButton;
   private javax.swing.JLabel crawlStatusLabel;
   private javax.swing.JTextField crawlStatusTextField;
   private javax.swing.JLabel crawlerHelpLabel;
   private javax.swing.JPanel crawlerPanel;
   private javax.swing.JMenuItem donateMenuItem;
   private javax.swing.JLabel filtersLabel;
   private javax.swing.JLabel filtersLabel1;
   private javax.swing.JMenuItem forumMenuItem;
   private javax.swing.JMenuItem helpContentsMenuItem;
   private javax.swing.JComboBox inclusionDomainComboBox;
   private javax.swing.JLabel inclusionDomainLabel;
   private javax.swing.JButton initiateNewCrawlButton;
   private javax.swing.JLabel jLabel1;
   private javax.swing.JLabel jLabel2;
   private javax.swing.JLabel jLabel3;
   private javax.swing.JLabel jLabel4;
   private javax.swing.JLabel jLabel5;
   private javax.swing.JPanel jPanel1;
   private javax.swing.JPanel jPanel10;
   private javax.swing.JPanel jPanel11;
   private javax.swing.JPanel jPanel2;
   private javax.swing.JPanel jPanel3;
   private javax.swing.JPanel jPanel4;
   private javax.swing.JPanel jPanel6;
   private javax.swing.JPanel jPanel7;
   private javax.swing.JPanel jPanel8;
   private javax.swing.JPanel jPanel9;
   private javax.swing.JScrollPane jScrollPane1;
   private javax.swing.JSeparator jSeparator1;
   private javax.swing.JSeparator jSeparator10;
   private javax.swing.JSeparator jSeparator2;
   private javax.swing.JSeparator jSeparator3;
   private javax.swing.JSeparator jSeparator4;
   private javax.swing.JSeparator jSeparator5;
   private javax.swing.JSeparator jSeparator7;
   private javax.swing.JSeparator jSeparator8;
   private javax.swing.JSeparator jSeparator9;
   private javax.swing.JTabbedPane jTabbedPane1;
   private javax.swing.JTextField jTextField1;
   private javax.swing.JTextField jTextField2;
   private javax.swing.JLabel launchCrawlerLogFileLabel;
   private javax.swing.JLabel launchSearchEngineLabel;
   private javax.swing.JLabel launchSearchEngineLogFileLabel;
   private javax.swing.JPanel mainPanel;
   private javax.swing.JMenuBar menuBar;
   private javax.swing.JTextField newURLTextField;
   private javax.swing.JTextField organizationNameTextField;
   private javax.swing.JTextField organizationURLTextField;
   private javax.swing.JButton removeURLButton;
   private javax.swing.JButton restartSearchEngineButton;
   private javax.swing.JCheckBox restartSearchEngineCheckBox;
   private javax.swing.JList rootURLsList;
   private DefaultListModel rootURLsListModel;
   private javax.swing.JLabel searchEngineHelpLabel;
   private javax.swing.JPanel searchEnginePanel;
   SpinnerNumberModel searchEnginePortModel = null;
   private javax.swing.JSpinner searchEnginePortNumberSpinner;
   private javax.swing.JLabel setAdminEMailLabel;
   private javax.swing.JButton setCrawlerCredentialsButton;
   private javax.swing.JLabel setOrganizationNameLabel;
   private javax.swing.JLabel setOrganizationURLLabel;
   private javax.swing.JLabel setSearchEnginePortLabel;
   private javax.swing.JTextField siteAdminEMailTextField;
   private javax.swing.JCheckBox skipDynamicURLsCheckBox;
   private javax.swing.JCheckBox skipEmailLinksCheckBox;
   private javax.swing.JCheckBox skipFTPSitesCheckBox;
   private javax.swing.JCheckBox skipLocalFilesCheckBox;
   private javax.swing.JCheckBox skipProbableLoopsCheckBox;
   private javax.swing.JLabel startingURLsLabel;
   private javax.swing.JButton stopCrawlButton;
   private javax.swing.JButton stopSearchEngineButton;
   private javax.swing.JLabel traversalDepthLabel;
   SpinnerNumberModel crawlerDepthModel = null;
   private javax.swing.JSpinner traversalDepthSpinner;
   private javax.swing.JButton updateAdminInfoButton;
   private javax.swing.JButton updateSearhEnginePortNumberButton;
   private javax.swing.JLabel viewCrawlerReportLabel;
   // End of variables declaration//GEN-END:variables

    // TODO: delete this? private final Timer messageTimer;
    // TODO: delete this? private final Timer busyIconTimer;
    // private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private String appRootDirectory = null;
    private JDialog aboutBox;
}
