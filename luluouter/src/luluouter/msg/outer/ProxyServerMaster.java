package luluouter.msg.outer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import luluouter.msg.inner.InnerMsgClient;

public class ProxyServerMaster {

    private static final ProxyServerMaster INSTANCE = new ProxyServerMaster();

    private Map<Integer, ProxyServer> proxyServers = new ConcurrentHashMap<Integer, ProxyServer>();

    public static ProxyServerMaster getInstance() {
        return INSTANCE;
    }

    private ProxyServerMaster() {

    }

    public void addInnerClient(int proxyPort, InnerMsgClient innerClient) {
        ProxyServer proxyServer = proxyServers.get(proxyPort);
        if (proxyServer == null) {
            proxyServer = new ProxyServer(proxyPort);

            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            executor.submit(proxyServer);
            executor.shutdown();

            proxyServers.put(proxyPort, proxyServer);
        }
        proxyServer.addInnerClient(innerClient);
    }
}
