package opensky.remotedeployer;

import java.io.BufferedReader;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.IOException;

import java.io.InputStream;

import java.io.InputStreamReader;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import oracle.ide.Context;
import oracle.ide.Ide;
import oracle.ide.controller.Controller;
import oracle.ide.controller.IdeAction;
import oracle.ide.wizard.WizardManager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BaseController implements Controller {
    
    public static final int SAMPLE_CMD_ID =
        Ide.findCmdID("opensky.remotedeployer.invokeAction");
    
    public boolean handleEvent(IdeAction ideAction, Context context) {

        try {
            if("".equals(SetupFrame.setupData.appPath+SetupFrame.setupData.domainPath+SetupFrame.setupData.weblogicPath)){
                SetupFrame frame=new SetupFrame();
                frame.setVisible(Boolean.TRUE);
            }
            String path=context.getSelection()[0].getLongLabel();
            SetupFrame.logln("\n Selected File Path is: "+path);
            File inputFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            
            NodeList nList=doc.getDocumentElement().getElementsByTagName("*");
            for(int i=0;i<nList.getLength();i++){
                if (nList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element)nList.item(i);
                    if("url".equals(e.getTagName())&&"jarURL".equals(e.getAttribute("n"))){
                        String jarLocationPath=e.getAttribute("path");
                        jarLocationPath=jarLocationPath.replace('\\', '/');
                        SetupFrame.logln("\nJar Location Path is:"+jarLocationPath);
                        path=path.replace('\\', '/');
                        String jprLocation=path.substring(0, path.lastIndexOf("/"));
                        if(new File(jarLocationPath).exists())
                            invokeJarReplacementScript(jarLocationPath);
                        else
                            invokeJarReplacementScript(jprLocation+"/"+jarLocationPath);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            SetupFrame.showError(e);
        }
//        WizardManager.getInstance().getWizard(BaseWizard.class).invoke(context);
        return true;
    }
    private void invokeJarReplacementScript(String jarPath) throws Exception{
        ArrayList<String> appNames=getAllSelectedApps();
        SetupFrame.logln("Jar File to replace is :"+jarPath);
        for(String appName:appNames){
            String appPath=SetupFrame.setupData.appPath+"/"+appName+"/app/"+appName;
            SetupFrame.logln("Application directory Path is:"+appPath);
            File appFile=new File(appPath);
            String destPath="";
            File jarFile=new File(jarPath);
            boolean isJarReplaced=false;
            if(!jarFile.exists()){
                SetupFrame.showError(new Exception("Source Jar Deployment does not exist"));
                return;
            }
            String jarName=jarFile.getName();
            if(!appFile.exists()){
                SetupFrame.showError(new Exception("No app Found inside the Weblogic"));
                return;
            }else{
                if(appFile.isDirectory()){
                    File files[]=appFile.listFiles();
                    for (File f: files){
                        int dotIndex=f.getName().lastIndexOf(".");
                        if(f.isDirectory()&&dotIndex>-1&&".war".equalsIgnoreCase(f.getName().substring(dotIndex))){
                            String uiPath=f.getAbsolutePath().replace('\\', '/')+"/WEB-INF/lib";
                            File file=new File(uiPath);
                            if(file.exists())
                                isJarReplaced= findAndReplaceFile(jarFile, file);
                            
                        }else if(f.isDirectory()&&"APP-INF".equalsIgnoreCase(f.getName())){
                            String uiPath=f.getAbsolutePath().replace('\\', '/')+"/lib";
                            File file=new File(uiPath);
                            if(file.exists())
                                isJarReplaced= findAndReplaceFile(jarFile, file);
                            if(isJarReplaced)
                                break;
                        }
                        
                    }
                }
                    if(isJarReplaced&&SetupFrame.setupData.immediateRedeploy)
                        new Redeployer().redeployApplication();
            }
        }
    }
    
    private boolean findAndReplaceFile(File source, File dest){
        FileInputStream fis= null;
        FileOutputStream fos= null;
        try{
            if(source.exists()&&!source.isDirectory()){
                String fileName=source.getName();
                String backupPath=dest.getAbsolutePath().replace('\\','/')+"/"+fileName+".backup";
                SetupFrame.logln("Backup File Path: "+backupPath);
                String destPath=dest.getAbsolutePath().replace('\\','/')+"/"+fileName;
                SetupFrame.logln("Destination path is: "+destPath);
                File backupFile=new File(backupPath);
                File[] files=dest.listFiles();
                for(File f:files){
                    if(fileName.equals(f.getName())){
                        if(!backupFile.exists()){
                            f.renameTo(backupFile);
                        }
                        File destFile=new File(destPath);
                        if(destFile.exists()){
                            destFile.delete();
                            destFile.createNewFile();
                        }
                        fis= new FileInputStream(source);
                        fos= new FileOutputStream(destFile);
                        
                        byte[] buffer=new byte[1024];
                        int length=0;
                        while ((length = fis.read(buffer)) > 0) {
                                    fos.write(buffer, 0, length);
                                }
                        SetupFrame.logln("Jar copying complete");
                        break;
                    }
                }
                
                return true;
            }else
                SetupFrame.showError(new Exception("Source File Not Found"));
        }catch(Exception e){
                e.printStackTrace();
                SetupFrame.showError(e);
        }finally{
            try{
            if(fis!=null)
                fis.close();
            if(fos!=null)
                fos.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
        }
        return false;
    }


    public boolean update(IdeAction ideAction, Context context) {
        ideAction.setEnabled(true);
        return true;
    }

    public static ArrayList<String> getAllSelectedApps() throws Exception {
        ArrayList<String> appNames=new ArrayList<String>();
        if("All".equals(SetupFrame.setupData.selectedApp)){
            File appSourcePath=new File(SetupFrame.setupData.appPath);
            if(appSourcePath.exists()&&appSourcePath.isDirectory()){
                File[] appFiles=appSourcePath.listFiles();
                for(File f:appFiles){
                    if(f.isDirectory()){
                        appNames.add(f.getName());
                    }
                }
            }
        }else{
            appNames.add(SetupFrame.setupData.selectedApp);
        }
        return appNames;
    }

    

}
