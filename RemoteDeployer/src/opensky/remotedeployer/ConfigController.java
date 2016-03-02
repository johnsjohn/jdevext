package opensky.remotedeployer;

import oracle.ide.Context;
import oracle.ide.controller.Controller;
import oracle.ide.controller.IdeAction;

public class ConfigController implements Controller {
    public boolean handleEvent(IdeAction ideAction, Context context) {
        SetupFrame.readMetaData();
        SetupFrame frame=new SetupFrame();
        frame.setVisible(Boolean.TRUE);
        return true;
    }

    public boolean update(IdeAction ideAction, Context context) {
        return false;
    }
}
