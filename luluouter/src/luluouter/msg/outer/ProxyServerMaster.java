package luluouter.msg.outer;

import java.util.HashMap;
import java.util.Map;

import luluouter.msg.inner.InnerClient;

public class ProxyServerMaster {

    private static final ProxyServerMaster INSTANCE = new ProxyServerMaster();

    private Map<Integer, ProxyServer> proxyServers = new HashMap<Integer, ProxyServer>();

    public static ProxyServerMaster getInstance() {
        return INSTANCE;
    }

    private ProxyServerMaster() {

    }

    public void addInnerClient(int proxyPort, InnerClient innerClient) {
        ProxyServer proxyServer = proxyServers.get(proxyPort);
        if (proxyServer == null) {
            proxyServer = new ProxyServer(proxyPort);
            proxyServers.put(proxyPort, proxyServer);
        }
        proxyServer.addInnerClient(innerClient);
    }
}
