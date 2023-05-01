!include MUI2.nsh

Name "WhelanLabs SearchEngine Manager v2.1"
OutFile "WhelanLabs_SearchEngineManager_Install.exe"

; The default installation directory
InstallDir C:\WhelanLabs

  Var StartMenuFolder
  
; The text to prompt the user to enter a directory
DirText "This will install the WhelanLabs Search Engine Manager on your computer. Choose a directory without spaces."

  ; Request application privileges for Windows Vista
  RequestExecutionLevel admin
  
!define MUI_HEADERIMAGE
!define MUI_HEADERIMAGE_BITMAP "${NSISDIR}\Contrib\Graphics\Header\orange.bmp"
!define MUI_HEADERIMAGE_UNBITMAP "${NSISDIR}\Contrib\Graphics\Header\orange-uninstall.bmp"
!define MUI_ABORTWARNING
  
!define MUI_ICON "${NSISDIR}\Contrib\Graphics\Icons\orange-install.ico"
!define MUI_UNICON "${NSISDIR}\Contrib\Graphics\Icons\orange-uninstall.ico"
!define MUI_WELCOMEFINISHPAGE_BITMAP "${NSISDIR}\Contrib\Graphics\Wizard\orange.bmp"
!define MUI_UNWELCOMEFINISHPAGE_BITMAP "${NSISDIR}\Contrib\Graphics\Wizard\orange-uninstall.bmp"


!define MUI_WELCOMEPAGE_TITLE_3LINES
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE "source/LICENSE.txt"
!insertmacro MUI_PAGE_STARTMENU Application $StartMenuFolder
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES

!define MUI_WELCOMEPAGE_TITLE_3LINES
!insertmacro MUI_UNPAGE_WELCOME
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES

!insertmacro MUI_LANGUAGE "English"
;======================================================
; Variables
  var varVersion
  var varCygwinDir
  var varJREDir
;======================================================
; Sections

Section "" ;No components page, name is not important

  StrCpy $varVersion "2.1"

  SetShellVarContext all

;${StrContains} $0 " " $INSTDIR
Push " "
Push "$INSTDIR"
 Call StrCSpn
Pop $R0 ; $R0 == !
StrCmp $R0 "" +3
 MessageBox MB_OK|MB_ICONEXCLAMATION 'Install directory CANNOT contain spaces. Install aborting.'
 quit


  ;
  ; Install Cygwin if needed
  ;

  ReadRegStr $0 HKCU "SOFTWARE\Cygnus Solutions\Cygwin\mounts v2\/" "native"
  ReadRegStr $1 HKLM "SOFTWARE\Cygnus Solutions\Cygwin\mounts v2\/" "native"
  StrCpy $varCygwinDir $0
  
  StrCmp $varCygwinDir "" 0 +13
    StrCpy $varCygwinDir $1
  StrCmp $varCygwinDir "" 0 +11
    MessageBox MB_OKCANCEL|MB_ICONQUESTION \
        "Installation requires Cygwin. Press OK to start Cygwin installation. (Use the default Cygwin options.)" \
        IDCANCEL cancelForCygwin IDOK okForCygwin
    cancelForCygwin:
        quit
    okForCygwin:    
        NSISdl::download /TIMEOUT=30000 http://www.cygwin.com/setup.exe $TEMP\cygwin_install_forWhelanLabsSearchEngine.exe
        ExecWait "$TEMP\cygwin_install_forWhelanLabsSearchEngine.exe"
        delete $TEMP\cygwin_install_forWhelanLabsSearchEngine.exe
    StrCmp "else" "" 0 +2 ; CygWin is installed
        DetailPrint 'Cygwin is already installed at $varCygwinDir.'

  ReadRegStr $0 HKCU "SOFTWARE\Cygnus Solutions\Cygwin\mounts v2\/" "native"
  ReadRegStr $1 HKLM "SOFTWARE\Cygnus Solutions\Cygwin\mounts v2\/" "native"
  StrCpy $varCygwinDir $0
  
  StrCmp $varCygwinDir "" 0 +5
    StrCpy $varCygwinDir $1
  StrCmp $varCygwinDir "" 0 +3
    MessageBox MB_OK|MB_ICONEXCLAMATION "Cygwin installation failed. The WhelanLabs Search Engine Manager installer will now exit."
    quit
  ; else




  ;
  ; Install JRE 1.6 or higher if needed.
  ;

  ReadRegStr $0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
  StrCmp $0 "1.6" 0 +9
  StrCmp $0 "1.7" 0 +8
  MessageBox MB_OKCANCEL|MB_ICONQUESTION "Installation requires Java JRE 1.6+. Press OK to start JRE installation." \
    IDCANCEL cancelForJava IDOK okForJava
  cancelForJava:
    quit
  okForJava:
    NSISdl::download /TIMEOUT=30000 http://javadl.sun.com/webapps/download/AutoDL?BundleId=23110 $TEMP\jre_install_forWhelanLabsSearchEngine.exe
    ExecWait "$TEMP\jre_install_forWhelanLabsSearchEngine.exe"
  ; continue
  
  delete $TEMP\jre_install_forWhelanLabsSearchEngine.exe
  
  ReadRegStr $0 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
  StrCmp $0 "1.6" 0 +4
  StrCmp $0 "1.7" 0 +3
  MessageBox MB_OK|MB_ICONEXCLAMATION "Java JRE 1.6+installation failed. The WhelanLabs Search Engine Manager installer will now exit."
  quit
  ; else
  
  ReadRegStr $varJREDir HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$0" "JavaHome"


  ;
  ; base installation
  ;
  
;MessageBox MB_OK|MB_ICONQUESTION    organization, nutchAdminEmail, nutchAdminEmail
  
  
  ; Set output path to the installation directory.
  SetOutPath "$INSTDIR\SearchEngine"

  ; Put file there
  File /r C:\sandbox\WhelanLabs_Search_Engine\output\*
  ;File C:\sandbox\WhelanLabs_Search_Engine\source\*
  
  WriteRegDWORD HKLM "Software\Cygnus Solutions\Cygwin\mounts v2\/whelanlabs"\
                    "flags" 0x00000002
  WriteRegStr HKLM "Software\Cygnus Solutions\Cygwin\mounts v2\/whelanlabs" \
                   "native" "$INSTDIR\SearchEngine\cygwin/whelanlabs"

  WriteRegStr HKLM "Software\WhelanLabs\SearchEngine" "ProgramDir" "$INSTDIR\SearchEngine"
  WriteRegStr HKLM "Software\WhelanLabs\SearchEngine" "Version" "$varVersion"

  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\WhelanLabsSearchEngine" \
                   "DisplayName" "WhelanLabs SearchEngine Manager"

  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\WhelanLabsSearchEngine" \
                   "UninstallString" "$INSTDIR\SearchEngine\Uninstall.exe"
                 
                                  
  !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
  CreateDirectory "$SMPROGRAMS\$StartMenuFolder"
  createShortCut "$SMPROGRAMS\$StartMenuFolder\SearchEngine Manager.lnk" "$INSTDIR\SearchEngine\startAdminConsole.bat" SW_SHOWMINIMIZED
  nsisStartMenu::RegenerateFolderW "$StartMenuFolder"
  createShortCut "$SMPROGRAMS\$StartMenuFolder\Documentation.lnk" "$INSTDIR\SearchEngine\helpText\MainHelp.htm"
  nsisStartMenu::RegenerateFolderW "$StartMenuFolder"
  createShortCut "$SMPROGRAMS\$StartMenuFolder\Uninstall.lnk" "$INSTDIR\SearchEngine\uninstall.exe"
  nsisStartMenu::RegenerateFolderW "$StartMenuFolder"
  !insertmacro MUI_STARTMENU_WRITE_END
  
  
  ; set @jre_dir@ in $INSTDIR\SearchEngine\startAdminConsole.bat
  Push @jre_dir@                                         #text to be replaced
  Push $varJREDir                                        #replace with
  Push all                                               #replace all occurrences
  Push all                                               #replace all occurrences
  Push $INSTDIR\SearchEngine\startAdminConsole.bat       #file to replace in
  Call AdvReplaceInFile                                  #call find and replace function
  
  ; set @version@ in $INSTDIR\SearchEngine\startAdminConsole.bat
  Push @version@                                         #text to be replaced
  Push $varVersion                                       #replace with
  Push all                                               #replace all occurrences
  Push all                                               #replace all occurrences
  Push $INSTDIR\SearchEngine\startAdminConsole.bat       #file to replace in
  Call AdvReplaceInFile                                  #call find and replace function

  ; set @install_dir@ var in $INSTDIR\SearchEngine\startAdminConsole.bat
  Push @install_dir@                                     #text to be replaced
  Push $INSTDIR\SearchEngine                             #replace with
  Push all                                               #replace all occurrences
  Push all                                               #replace all occurrences
  Push $INSTDIR\SearchEngine\startAdminConsole.bat       #file to replace in
  Call AdvReplaceInFile                                  #call find and replace function

  ; set @cygwin_dir@ var in $INSTDIR\SearchEngine\startAdminConsole.bat
  Push @cygwin_dir@                                       #text to be replaced
  Push $varCygwinDir                                      #replace with
  Push all                                                #replace all occurrences
  Push all                                                #replace all occurrences
  Push $INSTDIR\SearchEngine\startAdminConsole.bat        #file to replace in
  Call AdvReplaceInFile                                   #call find and replace function
  
  ; set @cygwin_dir@ var in $INSTDIR\SearchEngine\clean.bat
  Push @cygwin_dir@                                       #text to be replaced
  Push $varCygwinDir                                      #replace with
  Push all                                                #replace all occurrences
  Push all                                                #replace all occurrences
  Push $INSTDIR\SearchEngine\clean.bat                    #file to replace in
  Call AdvReplaceInFile                                   #call find and replace function
  
  ; set @base_dir@ var in $INSTDIR\SearchEngine\scripts\vars.bat
  Push @base_dir@                                         #text to be replaced
  Push $INSTDIR\SearchEngine                              #replace with
  Push all                                                #replace all occurrences
  Push all                                                #replace all occurrences
  Push $INSTDIR\SearchEngine\scripts\vars.bat             #file to replace in
  Call AdvReplaceInFile                                   #call find and replace function

  ; set @jre_dir@ var in $INSTDIR\SearchEngine\scripts\vars.bat
  Push @jre_dir@                                          #text to be replaced
  Push $varJREDir                                         #replace with
  Push all                                                #replace all occurrences
  Push all                                                #replace all occurrences
  Push $INSTDIR\SearchEngine\scripts\vars.bat             #file to replace in
  Call AdvReplaceInFile                                   #call find and replace function
  
  ; set @cygwin_dir@ var in $INSTDIR\SearchEngine\scripts\vars.bat
  Push @cygwin_dir@                                       #text to be replaced
  Push $varCygwinDir                                      #replace with
  Push all                                                #replace all occurrences
  Push all                                                #replace all occurrences
  Push $INSTDIR\SearchEngine\scripts\vars.bat             #file to replace in
  Call AdvReplaceInFile                                   #call find and replace function
  
  
  ; set @base_dir@ var in $INSTDIR\SearchEngine\AutomatedAdministration\StartServer.bat
  Push @base_dir@                                                         #text to be replaced
  Push $INSTDIR\SearchEngine                                              #replace with
  Push all                                                                #replace all occurrences
  Push all                                                                #replace all occurrences
  Push $INSTDIR\SearchEngine\AutomatedAdministration\StartServer.bat      #file to replace in
  Call AdvReplaceInFile                                                   #call find and replace function
  
  ; set @base_dir@ var in $INSTDIR\SearchEngine\AutomatedAdministration\RunCrawl.bat
  Push @base_dir@                                                         #text to be replaced
  Push $INSTDIR\SearchEngine                                              #replace with
  Push all                                                                #replace all occurrences
  Push all                                                                #replace all occurrences
  Push $INSTDIR\SearchEngine\AutomatedAdministration\RunCrawl.bat         #file to replace in
  Call AdvReplaceInFile                                                   #call find and replace function
  
  
  
  
  WriteUninstaller "$INSTDIR\SearchEngine\Uninstall.exe"

  goto end
	
;error:
;	DetailPrint "nsisStartMenu::RegenerateFolder failed"
;	Abort
    
end:
SectionEnd ; end the section
 
; The uninstall section
Section "Uninstall"

  SetShellVarContext all


  SetOutPath "$TEMP"

  FindWindow $0 "SunAwtFrame" "WhelanLabs SearchEngine"
  StrCmp $0 0 continueInstall
    MessageBox MB_ICONSTOP|MB_OK "The WhelanLabs SearchEngine Manager is running. Close it and try again."
    Abort
  continueInstall:

    
  ExecWait "$varCygwinDir\bin\bash.exe --login /whelanlabs/searchengine/stopCrawl.sh"
  ExecWait "$varCygwinDir\bin\bash.exe --login /whelanlabs/searchengine/stopNutch.sh"
  
  delete "$INSTDIR\Uninstall.exe"
  RMDir /r "$INSTDIR"
  RMDir "$INSTDIR\.."
  DeleteRegKey HKLM "Software\Cygnus Solutions\Cygwin\mounts v2\/whelanlabs"
  DeleteRegKey HKLM "Software\WhelanLabs\SearchEngine"
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\WhelanLabsSearchEngine"
  DeleteRegKey /ifempty HKLM "Software\WhelanLabs"
  
  !insertmacro MUI_STARTMENU_GETFOLDER Application $StartMenuFolder
  
  delete "$SMPROGRAMS\$StartMenuFolder\SearchEngine Manager.lnk"
  delete "$SMPROGRAMS\$StartMenuFolder\Uninstall.lnk"
  delete "$SMPROGRAMS\$StartMenuFolder\Documentation.lnk"
  RMDir "$SMPROGRAMS\$StartMenuFolder"
  
MessageBox MB_YESNO|MB_ICONQUESTION \
    "Do you want to also uninstall Cygwin?" \
    IDYES uninstallCygwin IDNO keepCygwin
uninstallCygwin:
  ReadRegStr $0 HKCU "SOFTWARE\Cygnus Solutions\Cygwin\mounts v2\/" "native"
  ReadRegStr $1 HKLM "SOFTWARE\Cygnus Solutions\Cygwin\mounts v2\/" "native"
  StrCmp $0 "" +3 0
    RMDir /r "$0"
    DeleteRegKey HKCU "SOFTWARE\Cygnus Solutions\Cygwin"
  StrCmp $1 "" +3 0
    RMDir /r "$1"
    DeleteRegKey HKLM "SOFTWARE\Cygnus Solutions\Cygwin"
keepCygwin:

SectionEnd 



;======================================================
; Functions

Function AdvReplaceInFile
         Exch $0 ;file to replace in
         Exch
         Exch $1 ;number to replace after
         Exch
         Exch 2
         Exch $2 ;replace and onwards
         Exch 2
         Exch 3
         Exch $3 ;replace with
         Exch 3
         Exch 4
         Exch $4 ;to replace
         Exch 4
         Push $5 ;minus count
         Push $6 ;universal
         Push $7 ;end string
         Push $8 ;left string
         Push $9 ;right string
         Push $R0 ;file1
         Push $R1 ;file2
         Push $R2 ;read
         Push $R3 ;universal
         Push $R4 ;count (onwards)
         Push $R5 ;count (after)
         Push $R6 ;temp file name
         GetTempFileName $R6
         FileOpen $R1 $0 r ;file to search in
         FileOpen $R0 $R6 w ;temp file
                  StrLen $R3 $4
                  StrCpy $R4 -1
                  StrCpy $R5 -1
        loop_read:
         ClearErrors
         FileRead $R1 $R2 ;read line
         IfErrors exit
         StrCpy $5 0
         StrCpy $7 $R2
 
        loop_filter:
         IntOp $5 $5 - 1
         StrCpy $6 $7 $R3 $5 ;search
         StrCmp $6 "" file_write2
         StrCmp $6 $4 0 loop_filter
 
         StrCpy $8 $7 $5 ;left part
         IntOp $6 $5 + $R3
         StrCpy $9 $7 "" $6 ;right part
         StrCpy $7 $8$3$9 ;re-join
 
         IntOp $R4 $R4 + 1
         StrCmp $2 all file_write1
         StrCmp $R4 $2 0 file_write2
         IntOp $R4 $R4 - 1
 
         IntOp $R5 $R5 + 1
         StrCmp $1 all file_write1
         StrCmp $R5 $1 0 file_write1
         IntOp $R5 $R5 - 1
         Goto file_write2
 
        file_write1:
         FileWrite $R0 $7 ;write modified line
         Goto loop_read
 
        file_write2:
         FileWrite $R0 $R2 ;write unmodified line
         Goto loop_read
 
        exit:
         FileClose $R0
         FileClose $R1
 
         SetDetailsPrint none
         Delete $0
         Rename $R6 $0
         Delete $R6
         SetDetailsPrint both
 
         Pop $R6
         Pop $R5
         Pop $R4
         Pop $R3
         Pop $R2
         Pop $R1
         Pop $R0
         Pop $9
         Pop $8
         Pop $7
         Pop $6
         Pop $5
         Pop $4
         Pop $3
         Pop $2
         Pop $1
         Pop $0
FunctionEnd

Function StrCSpn
 Exch $R0 ; string to check
 Exch
 Exch $R1 ; string of chars
 Push $R2 ; current char
 Push $R3 ; current char
 Push $R4 ; char loop
 Push $R5 ; char loop
 
  StrCpy $R4 -1
 
  NextChar:
  StrCpy $R2 $R1 1 $R4
  IntOp $R4 $R4 - 1
   StrCmp $R2 "" StrOK
 
   StrCpy $R5 -1
 
   NextCharCheck:
   StrCpy $R3 $R0 1 $R5
   IntOp $R5 $R5 - 1
    StrCmp $R3 "" NextChar
    StrCmp $R3 $R2 0 NextCharCheck
     StrCpy $R0 $R2
     Goto Done
 
 StrOK:
 StrCpy $R0 ""
 
 Done:
 
 Pop $R5
 Pop $R4
 Pop $R3
 Pop $R2
 Pop $R1
 Exch $R0
FunctionEnd
