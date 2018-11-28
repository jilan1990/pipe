package luluouter.msg.outer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
            proxyServers.put(proxyPort, proxyServer);
        }
        proxyServer.addInnerClient(innerClient);
    }
}
