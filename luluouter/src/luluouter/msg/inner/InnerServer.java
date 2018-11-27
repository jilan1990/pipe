package luluouter.msg.inner;

import java.net.ServerSocket;
import java.net.Socket;

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
                InnerClient innerClient = new InnerClient(client);

                innerClient.init();
            }    
        } catch (Exception e) {    
            System.out.println("InnerServer.init: " + e.getMessage());
        }
	}
}
