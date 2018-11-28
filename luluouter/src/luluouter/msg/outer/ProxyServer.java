package luluouter.msg.outer;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import luluouter.msg.inner.InnerMsgClient;

public class ProxyServer {
    private Map<String, InnerMsgClient> innerMsgClients = new ConcurrentHashMap<String, InnerMsgClient>();
    private int proxyPort;

    public ProxyServer(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public void init() {
        try (ServerSocket serverSocket = new ServerSocket(proxyPort);) {
            while (true) {
                Socket outerClient = serverSocket.accept();

                ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                executor.submit(() -> {
                    InnerMsgClient innerMsgClient = null;
                    // ignore the errors which will disappear after retry
                    for (Map.Entry<String, InnerMsgClient> entry : innerMsgClients.entrySet()) {
                        innerMsgClient = entry.getValue();
                    }
                    innerMsgClient.newSocket(proxyPort);
                });
                executor.shutdown();
            }
        } catch (Exception e) {
            System.out.println("ProxyServer.init: " + e.getMessage());
        }
    }

    public void addInnerClient(InnerMsgClient innerClient) {
        innerMsgClients.put(innerClient.getKey(), innerClient);
    }

}
