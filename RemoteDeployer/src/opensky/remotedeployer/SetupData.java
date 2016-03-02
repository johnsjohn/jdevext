package opensky.remotedeployer;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class SetupData implements Serializable {
    
    public  String weblogicPath="";
    public  String domainPath="";
    public  String appPath="";
    public String selectedDomain="";
    public String selectedApp="";
    
    public String host="127.0.0.1";
    public String port="7011";
    public String username="weblogic";
    public String password="weblogic1";
    String weblogicClassPath="";
    boolean immediateRedeploy=false;

    SetupData(){
    }
    

}
