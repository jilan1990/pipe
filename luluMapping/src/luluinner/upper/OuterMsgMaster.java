package luluinner.upper;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import luluinner.model.MappingModel;
import luluinner.model.MappingsModel;
import luluinner.msg.upper.OuterMsgServer;

public class OuterMsgMaster {


    private static final OuterMsgMaster INSTANCE = new OuterMsgMaster();

    private OuterMsgMaster() {
    }

    public static OuterMsgMaster getInstance() {
        return INSTANCE;
    }

    public void init(MappingsModel mappingsModel) {

        List<MappingModel> mappings = mappingsModel.getMappings();

        for (MappingModel mapping : mappings) {
            int mappingport = mapping.getMappingport();
            String shyip = mapping.getShyip();
            int shyport = mapping.getShyport();
            OuterMsgServer outerMsgServer = new OuterMsgServer(mappingport, shyip, shyport);

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(outerMsgServer);
            executor.shutdown();
        }

    }

}
