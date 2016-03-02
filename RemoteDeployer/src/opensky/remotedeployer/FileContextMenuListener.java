package opensky.remotedeployer;

import oracle.ide.Context;
import oracle.ide.controller.ContextMenu;
import oracle.ide.controller.ContextMenuListener;
import oracle.ide.controller.IdeAction;

public class FileContextMenuListener implements ContextMenuListener {
    public void menuWillShow(ContextMenu contextMenu) {
        String label=contextMenu.getContext().getElement().getLongLabel();
        if(label==null||"".equals(label))
            return;
        if(label.lastIndexOf(".")<0)
            return;
        
        if("".equals(SetupFrame.setupData.appPath+SetupFrame.setupData.domainPath+SetupFrame.setupData.weblogicPath)){
            SetupFrame.readMetaData();
        }
        if(".jpr".equalsIgnoreCase(label.substring(label.lastIndexOf(".")))){
            IdeAction action =IdeAction.find(BaseController.SAMPLE_CMD_ID);
            if(SetupFrame.setupData.immediateRedeploy)
                action.setName("Copy deployed Jar to Standalone (Immediate Redeploy)");
            else
                action.setName("Copy deployed Jar to Standalone");
            contextMenu.add(contextMenu.createMenuItem(action));
        }
    }

    public void menuWillHide(ContextMenu contextMenu) {
    }

    public boolean handleDefaultAction(Context context) {
        return false;
    }
}
