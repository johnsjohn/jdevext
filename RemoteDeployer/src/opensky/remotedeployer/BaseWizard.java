package opensky.remotedeployer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;

import java.io.FileWriter;

import javax.ide.command.Context;
import javax.ide.wizard.Wizard;

import javax.swing.Icon;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.parsers.ParserConfigurationException;

import oracle.javatools.icons.OracleIcons;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BaseWizard implements Wizard {
    public boolean invoke(Context context) {
        
        
        
        return true;
    }

    public boolean isAvailable(Context context) {
        return true;
    }
    
    public Icon getIcon() {
        return OracleIcons.getIcon(OracleIcons.ACTION);
    }


}
