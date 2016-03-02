package opensky.remotedeployer;

import java.awt.Dimension;

import java.awt.Rectangle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;

import java.io.ObjectOutputStream;

import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.ArrayList;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultButtonModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.UIManager.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import oracle.ide.log.LogManager;
import oracle.ide.log.MessagePage;

public class SetupFrame extends JFrame {
    private JButton cancelButton = new JButton();
    private JButton saveButton = new JButton();
    private JTextField standAlonePathTextField = new JTextField();
    private JLabel standAlonePathLabel = new JLabel();
    private JButton browseButton = new JButton();

    private JComboBox domainNameComboBox = new JComboBox();
    private JLabel domainNameLabel = new JLabel();
    private JComboBox applicationComboBox = new JComboBox();
    private JLabel applicationLabel = new JLabel();

    public static SetupData setupData=new SetupData();
    private JTextField weblogicHostTextField = new JTextField();
    private JLabel weblogicHostLabel = new JLabel();
    private JLabel weblogicPortLabel = new JLabel();
    private JTextField weblogicPortTextField = new JTextField();
    private JLabel weblogicUserNameLabel = new JLabel();
    private JTextField weblogicUserNameTextField = new JTextField();
    private JLabel weblogicPasswordLabel = new JLabel();
    private JTextField weblogicPasswordField = new JTextField();
    private JLabel wlClassPathLabel = new JLabel();
    private JTextField weblogicClassPathField = new JTextField();
    private JCheckBox redeployEnabledCheckbox = new JCheckBox();
    private JButton redeployButton = new JButton();

    public SetupFrame() {
        try {
//            readMetaData();
            initSetupData();
            if(setupData==null){
                setupData=new SetupData();
            }
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        
//        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.getContentPane().setLayout( null );
        this.setSize(new Dimension(354, 488));
        this.setTitle( "Remote Deployer: Confugurations" );
        this.getContentPane().add(redeployButton, null);
        this.getContentPane().add(redeployEnabledCheckbox, null);
        this.getContentPane().add(weblogicClassPathField, null);
        this.getContentPane().add(wlClassPathLabel, null);
        this.getContentPane().add(weblogicPasswordField, null);
        this.getContentPane().add(weblogicPasswordLabel, null);
        this.getContentPane().add(weblogicUserNameTextField, null);
        this.getContentPane().add(weblogicUserNameLabel, null);
        this.getContentPane().add(weblogicPortTextField, null);
        this.getContentPane().add(weblogicPortLabel, null);
        this.getContentPane().add(weblogicHostLabel, null);
        this.getContentPane().add(weblogicHostTextField, null);
        this.getContentPane().add(applicationLabel, null);
        this.getContentPane().add(applicationComboBox, null);
        this.getContentPane().add(domainNameLabel, null);
        this.getContentPane().add(domainNameComboBox, null);
        this.getContentPane().add(browseButton, null);
        this.getContentPane().add(standAlonePathLabel, null);
        this.getContentPane().add(standAlonePathTextField, null);
        this.getContentPane().add(saveButton, null);
        this.getContentPane().add(cancelButton, null);
        cancelButton.setText("Cancel");
        cancelButton.setBounds(new Rectangle(260, 420, 75, 21));
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cancelActionPerformed(e);
                }
            });
        saveButton.setText("Save");
        saveButton.setBounds(new Rectangle(175, 420, 75, 21));
        saveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    saveActionPerformed(e);
                }
            });
        standAlonePathTextField.setBounds(new Rectangle(110, 10, 210, 20));
        standAlonePathTextField.addFocusListener(new FocusAdapter() {
                public void focusLost(FocusEvent e) {
                    standAlonePathTextFieldFocusLost(e);
                }
            });
        standAlonePathTextField.getDocument().addDocumentListener(new DocumentListener() {
            // implement the methods

                public void insertUpdate(DocumentEvent e) {
                    standAlonePathTextFieldTextChanged();
                }

                public void removeUpdate(DocumentEvent e) {
                    
                }

                public void changedUpdate(DocumentEvent e) {
                    standAlonePathTextFieldTextChanged();
                }

            });
        domainNameComboBox.addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent e) {
                    domainNameComboBoxActionPerformed(e);
                }
            });
        applicationComboBox.addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent e) {
                    applicationComboBoxActionPerformed(e);
                }
            });
        weblogicHostTextField.setBounds(new Rectangle(110, 215, 185, 20));
        weblogicHostLabel.setText("Host");
        weblogicHostLabel.setBounds(new Rectangle(50, 220, 34, 14));
        weblogicPortLabel.setText("Port");
        weblogicPortLabel.setBounds(new Rectangle(50, 250, 34, 14));
        weblogicPortTextField.setBounds(new Rectangle(110, 245, 50, 20));
        weblogicUserNameLabel.setText("Username");
        weblogicUserNameLabel.setBounds(new Rectangle(50, 280, 55, 15));
        weblogicUserNameTextField.setBounds(new Rectangle(110, 275, 185, 20));
        weblogicPasswordLabel.setText("Password");
        weblogicPasswordLabel.setBounds(new Rectangle(50, 310, 55, 15));
        weblogicPasswordField.setBounds(new Rectangle(110, 305, 185, 20));
        wlClassPathLabel.setText("Weblogic Classpath");
        wlClassPathLabel.setBounds(new Rectangle(5, 340, 110, 15));
        weblogicClassPathField.setBounds(new Rectangle(110, 335, 185, 20));
        redeployEnabledCheckbox.setText("Enable immediate weblogic app redeployment");

        redeployEnabledCheckbox.setBounds(new Rectangle(95, 375, 240, 20));
        redeployEnabledCheckbox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    redeployEnabledCheckboxActionPerformed(e);
                }
            });
        redeployEnabledCheckbox.setSelected(setupData.immediateRedeploy);
        redeployButton.setText("Redeploy");
        redeployButton.setBounds(new Rectangle(75, 420, 90, 20));
        redeployButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    redeployButtonActionPerformed(e);
                }
            });
        weblogicClassPathField.setText(setupData.weblogicClassPath);
        weblogicUserNameTextField.setText(setupData.username);
        weblogicHostTextField.setText(setupData.host);
        weblogicPortTextField.setText(setupData.port);
        weblogicPasswordField.setText(new String(setupData.password));

        cancelButton.setModel(new DefaultButtonModel());
        standAlonePathTextField.setText(setupData.weblogicPath);
        standAlonePathLabel.setText("Weblogic Path");
        standAlonePathLabel.setBounds(new Rectangle(15, 15, 85, 15));
        browseButton.setText("Browse");
        browseButton.setBounds(new Rectangle(235, 40, 85, 21));
        browseButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    browseButtonActionPerformed(e);
                }
            });
        domainNameComboBox.setBounds(new Rectangle(110, 90, 210, 20));
        domainNameLabel.setText("Domain");
        domainNameLabel.setBounds(new Rectangle(15, 95, 70, 15));
        applicationComboBox.setBounds(new Rectangle(110, 155, 210, 20));
        applicationLabel.setText("Application");
        applicationLabel.setBounds(new Rectangle(20, 160, 70, 15));
        
        
        
    }

    private void saveActionPerformed(ActionEvent e) {
        SetupFrame.setupData.host=weblogicHostTextField.getText();
        SetupFrame.setupData.password=weblogicPasswordField.getText();
        SetupFrame.setupData.username=weblogicUserNameTextField.getText();
        SetupFrame.setupData.port=weblogicPortTextField.getText();
        writeMetaData();
        this.dispose();
    }

    private void cancelActionPerformed(ActionEvent e) {
        this.dispose();
    }

    private void browseButtonActionPerformed(ActionEvent e) {
        
        System.out.println("\nThe weblogic path retrieved is: "+setupData.weblogicPath);
        JFileChooser fileChooser = new JFileChooser(setupData.weblogicPath);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if(fileChooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
            File file=fileChooser.getSelectedFile();
            setupData.weblogicPath= file.getAbsolutePath();
            setupData.weblogicPath=setupData.weblogicPath.replace('\\','/');
            standAlonePathTextField.setText(setupData.weblogicPath);
        }
    }
    public static void readMetaData() {
        setupData=new SetupData();
        ObjectInputStream ois=null;
        FileInputStream fis= null;
        try{
            File metaFile=new File(System.getProperty("user.home")+"/remoteDeployerMetaData.txt");
            if(!metaFile.exists()){
                metaFile.createNewFile();
                return;
            }
            fis=new FileInputStream(metaFile);
            ois= new ObjectInputStream(fis);
            setupData = (SetupData)ois.readObject();
        }catch(EOFException ex){
        }
        catch(Exception e){
            e.printStackTrace();
            showError(e);
            
            }finally{
                try{
                    if(fis!=null)
                        fis.close();
                    if(ois!=null)
                        ois.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
    }

    private void writeMetaData() {
        ObjectOutputStream oos=null;
        FileOutputStream fos= null;
        try{
            File metaFile=new File(System.getProperty("user.home")+"/remoteDeployerMetaData.txt");
            if(!metaFile.exists()){
                metaFile.createNewFile();
            }
            fos=new FileOutputStream(metaFile);
            oos= new ObjectOutputStream(fos);
            oos.writeObject(setupData);
            
        }catch(Exception e){
            e.printStackTrace();
            showError(e);
            }finally{
                try{
                    if(fos!=null)
                        fos.close();
                    if(oos!=null)
                        oos.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
    }

    private void populateDomainPath() throws Exception{
        setupData.domainPath=setupData.weblogicPath+"/user_projects/domains";
        
        File domainFile=new File(setupData.domainPath);
        if(domainFile.exists()){
            File domainFiles[]=domainFile.listFiles();
            domainNameComboBox.removeAllItems();
            
            for(File f:domainFiles){
                if(f.exists()&& f.isDirectory()){
                    domainNameComboBox.addItem(f.getName());
                    if(setupData.selectedDomain==null||"".equals(setupData.selectedDomain))
                        setupData.selectedDomain=f.getName();
                }
            }
            if(domainNameComboBox.getItemCount()>0)
                domainNameComboBox.setSelectedItem(setupData.selectedDomain);
        }
        else
            JOptionPane.showMessageDialog(this, "No valid domains found", "Invalid Path", JOptionPane.WARNING_MESSAGE);
    }

    private void populateAppPath() throws Exception {
        setupData.appPath=setupData.domainPath+"/"+domainNameComboBox.getSelectedItem()+"/servers/AdminServer/upload";
        
        System.out.println("\nApplications Path: "+setupData.appPath);
        File appFile=new File(setupData.appPath);
        applicationComboBox.removeAllItems();
        String appTemp=setupData.selectedApp;
        applicationComboBox.addItem("All");
        setupData.selectedApp=appTemp;
        if(appFile.exists()){
            if(appFile.isDirectory()){
                File []appFiles= appFile.listFiles(); 
                for(File f:appFiles){
                    if(f.exists()&& f.isDirectory()){
                        applicationComboBox.addItem(f.getName());
                        
                    }
                }
                if(setupData.selectedApp==null||"".equals(setupData.selectedApp))
                    setupData.selectedApp="All";
                if(applicationComboBox.getItemCount()>0)
                    applicationComboBox.setSelectedItem(setupData.selectedApp);
            }
        }else
            JOptionPane.showMessageDialog(this, "No valid Applications found", "Invalid Path", JOptionPane.WARNING_MESSAGE);
    }

    private void initSetupData() throws Exception{
        
        if(setupData!=null){
            if("".equals(setupData.weblogicPath)){
                logln("Default standalone path retrieved as: "+System.getenv("$MW_HOME_STANDALONE"));
                setupData.weblogicPath=System.getenv("MW_HOME_STANDALONE");
                
            }
            if(setupData.weblogicPath!=null&&!"".equals(setupData.weblogicPath)){
                File file=new File(setupData.weblogicPath);
                System.out.println("\nThe weblogic path retrieved is: "+setupData.weblogicPath);
                if(file.isDirectory()){
                    if("".equals(setupData.domainPath)){    
                        populateDomainPath();
                        if("".equals(setupData.appPath)){
                            populateAppPath();
                        }
                    }
                    
                }
            }
            else{
                setupData.weblogicPath=System.getProperty("user.home");
            }
            
        }
    }

    private void domainNameComboBoxActionPerformed(ItemEvent e) {
        if(e.getStateChange() == ItemEvent.SELECTED){
            setupData.selectedDomain = (String)domainNameComboBox.getSelectedItem();
            try {
                populateAppPath();
            } catch (Exception f) {
                showError(f);
            }
        }
    }

    private void applicationComboBoxActionPerformed(ItemEvent e) {
        if(e.getStateChange() == ItemEvent.SELECTED){
            setupData.selectedApp=(String)applicationComboBox.getSelectedItem();
        }
    }
    
    private void standAlonePathTextFieldTextChanged() {
        
        try {
            String path=standAlonePathTextField.getText();
            if(!new File(path).exists()){
                JOptionPane.showMessageDialog(this, "Not a valid Weblogic Standalone Path", "Invalid Path", JOptionPane.WARNING_MESSAGE);
            }
            populateDomainPath();
            setWeblogicClassPath();
        } catch (Exception e) {
            showError(e);
        }
    }

    private void standAlonePathTextFieldFocusLost(FocusEvent e) {
        
        try {
            String path=standAlonePathTextField.getText();
            if(path.equals(setupData.weblogicPath))
                return;
            if(!new File(path).exists()){
                JOptionPane.showMessageDialog(this, "Not a valid Weblogic Standalone Path", "Invalid Path", JOptionPane.WARNING_MESSAGE);
            }
            populateDomainPath();
            setWeblogicClassPath();
        } catch (Exception f) {
            showError(f);
        }
    }

    public static void showError(Exception e) {
        JOptionPane.showMessageDialog(null, e,"Error Occured", JOptionPane.ERROR_MESSAGE);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        
        logln(sw.toString());
    }
    public static  void logln( String message ) {
        if(message==null)
            return;
        LogManager lm = LogManager.getLogManager();
        lm.showLog();
        lm.getMsgPage().log( message + "\n" );
    }
    private void setWeblogicClassPath(){
        if(SetupFrame.setupData.weblogicPath==null||"".equals(SetupFrame.setupData.weblogicPath))
            return;
        File weblogicFile=new File(SetupFrame.setupData.weblogicPath);
        if(!weblogicFile.exists()){
           return;
        }        
        SetupFrame.setupData.weblogicClassPath =SetupFrame.setupData.weblogicPath+"/wlserver_10.3/server/lib/weblogic.jar";
        weblogicClassPathField.setText(SetupFrame.setupData.weblogicClassPath);
    }

    private void redeployEnabledCheckboxActionPerformed(ActionEvent e) {
        setupData.immediateRedeploy=redeployEnabledCheckbox.isSelected();
    }

    private void redeployButtonActionPerformed(ActionEvent e) {
        new Redeployer().redeployApplication();
    }
            
    
}
