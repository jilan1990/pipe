package luluouter.data.inner;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InnerDataMaster {

    private static final InnerDataMaster INSTANCE = new InnerDataMaster();

    private Map<Map<String, Object>, InnerDataClient> innerDataClients = new ConcurrentHashMap<Map<String, Object>, InnerDataClient>();

    public static InnerDataMaster getInstance() {
        return INSTANCE;
    }

    private InnerDataMaster() {

    }

    public void addDataClient(Map<String, Object> map, InnerDataClient innerDataClient) {
        innerDataClients.put(map, innerDataClient);
    }

    public InnerDataClient getInnerDataClient(Map<String, Object> map) {
        return innerDataClients.remove(map);
    }
}
