package luluouter;

import java.util.Map;

import luluouter.config.ConfigLoad;
import luluouter.data.inner.InnerDataServer;
import luluouter.msg.inner.InnerMsgServer;

public class OuterMaster {

	public static void main(String[] args) {

        ConfigLoad configLoad = new ConfigLoad();
        Map<String, Object> configs = configLoad.loadConfig();
        if (configs == null) {
            return;
        }

        int msg_port = (int) configs.get("msg_port");
        int data_port = (int) configs.get("data_port");
        
        System.out.println("InnerMsgServer ...\n");
        InnerDataServer innerDataServer = new InnerDataServer(data_port);
        innerDataServer.init();

        System.out.println("InnerMsgServer ...\n");
        InnerMsgServer innerServer = new InnerMsgServer(msg_port);
		innerServer.init();
	}

}
