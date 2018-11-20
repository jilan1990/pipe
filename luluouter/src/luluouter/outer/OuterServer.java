package luluouter.outer;

import java.net.ServerSocket;
import java.net.Socket;

public class OuterServer {
    public static final int SERVER_PORT = 80;

	public void init() {    
        try {    
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);    
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
