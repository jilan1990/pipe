package luluouter.inner;

import java.net.ServerSocket;
import java.net.Socket;

public class InnerServer {
    public static final int INTER_PORT = 8080;

	public void init() {    
        try {    
            ServerSocket serverSocket = new ServerSocket(INTER_PORT);    
            while (true) {    
                // һ���ж���, ���ʾ��������ͻ��˻��������    
                Socket client = serverSocket.accept();    
                // �����������    
                new HandlerThread(client);    
            }    
        } catch (Exception e) {    
            System.out.println("�������쳣: " + e.getMessage());    
        }
	}
}
