package luluinner;

import luluinner.model.MappingsModel;
import luluinner.upper.OuterMsgMaster;
import luluinner.util.YamlUtil;

public class StartUp {

    public static void main(String[] args) {

        MappingsModel mappingsModel = YamlUtil.getYamlObj("conf/mapping.yaml");

        if (mappingsModel == null) {
            return;
        }

        OuterMsgMaster.getInstance().init(mappingsModel);
    }

}
