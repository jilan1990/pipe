package luluouter.msg.outer;

import java.net.ServerSocket;
import java.net.Socket;

import luluouter.msg.inner.InnerClient;

public class ProxyServer {
    private int proxyPort;

    public ProxyServer(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public void init() {
        try {
            ServerSocket serverSocket = new ServerSocket(proxyPort);
            while (true) {
                Socket client = serverSocket.accept();
                // new ProxyWorker(client);
            }
        } catch (Exception e) {
            System.out.println("ProxyServer.init: " + e.getMessage());
        }
    }

    public void addInnerClient(InnerClient innerClient) {
        // TODO Auto-generated method stub

    }

}
