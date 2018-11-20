package luluouter.inner;

import java.net.ServerSocket;
import java.net.Socket;

public class InnerServer {
    public static final int INTER_PORT = 8080;

	public void init() {    
        try {    
            ServerSocket serverSocket = new ServerSocket(INTER_PORT);    
            while (true) {    
                // 一旦有堵塞, 则表示服务器与客户端获得了连接    
                Socket client = serverSocket.accept();    
                // 处理这次连接    
                new HandlerThread(client);    
            }    
        } catch (Exception e) {    
            System.out.println("服务器异常: " + e.getMessage());    
        }
	}
}
