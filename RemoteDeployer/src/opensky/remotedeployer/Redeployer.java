package opensky.remotedeployer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;

public class Redeployer implements Runnable {
    public void redeployApplication(){
        
        Thread t=new Thread(this);
        t.start();
    }

    
    public void run() {
        InputStream is=null;
        BufferedReader br=null;
        
        InputStream eis=null;
        BufferedReader ebr=null;
        
        try {
            SetupFrame.logln("redeploying app in weblogic server");
            ArrayList<String> appNames=BaseController.getAllSelectedApps();
            for(String appName:appNames){
                String cmd[]={"java", "-cp",SetupFrame.setupData.weblogicClassPath,"weblogic.Deployer","-adminurl", "http://"+
                           SetupFrame.setupData.host+":"+SetupFrame.setupData.port,
                           "-user",SetupFrame.setupData.username,"-password",SetupFrame.setupData.password,"-redeploy","-name",appName};
                for(String c:cmd)
                    SetupFrame.logln(c);
                
                ProcessBuilder pb = new ProcessBuilder(cmd);
                Process p;
        
                p = pb.start();
                
                is = p.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));
                String line = null;
                while (br.ready()&&(line = br.readLine()) != null) {
                  SetupFrame.logln(line);
                }
                eis = p.getErrorStream();
                ebr = new BufferedReader(new InputStreamReader(eis));
                String eline = null;
                String errDetails="";
                while (ebr.ready()&&(eline = ebr.readLine()) != null) {
                  errDetails=eline+"\n";
                }
                if(!"".equals(errDetails))
                    SetupFrame.showError(new Exception(errDetails));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            SetupFrame.showError(e);
        }finally{
                try{
                    if(is!=null)
                        is.close();
                    if(br!=null)
                        br.close();
                    if(eis!=null)
                        eis.close();
                    if(ebr!=null)
                        ebr.close();
                }catch(Exception e){
                    SetupFrame.logln(e.getMessage());
                }
            }
    }
}
