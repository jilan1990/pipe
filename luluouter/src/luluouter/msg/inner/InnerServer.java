package luluouter.msg.inner;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class InnerServer {
    private int port;

    public InnerServer(int port) {
        this.port = port;
    }

    public void init() {

        try (ServerSocket serverSocket = new ServerSocket(port);) {
            System.out.println("listening:" + port);
            while (true) {    
                //
                Socket client = serverSocket.accept();    
                //
                ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                executor.submit(() -> {
                    InnerClient innerClient = InnerClient.createClient(client);
                    innerClient.init();
                });
                executor.shutdown();
            }    
        } catch (Exception e) {    
            System.out.println("InnerServer.init: " + e.getMessage());
        }
	}
}
