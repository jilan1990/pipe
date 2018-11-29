package luluinner;

import java.util.Map;

import luluinner.config.ConfigLoad;
import luluinner.upper.UpperAgentMaster;

public class StartUp {

    public static void main(String[] args) {

        ConfigLoad configLoad = new ConfigLoad();
        Map<String, Object> configs = configLoad.loadConfig();
        if (configs == null) {
            return;
        }

        UpperAgentMaster.getInstance().init(configs);
    }

}
