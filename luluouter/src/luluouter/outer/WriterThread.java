package luluouter.outer;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class WriterThread  implements Runnable {
	
	private Socket socket;
    
	BlockingQueue<byte[]> queue = new LinkedBlockingQueue<byte[]>();
	
	public WriterThread(Socket client) {    
        socket = client;    
        new Thread(this).start();    
    }

    

    public void run() {    
        try {    
            // ��ȡ�ͻ�������    

            // ��ͻ��˻ظ���Ϣ    
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());    
            System.out.print("������:\t");    
            // ���ͼ��������һ��    
            String s = new BufferedReader(new InputStreamReader(System.in)).readLine();    
            out.writeUTF(s);    
              
            out.close();    
        } catch (Exception e) {    
            System.out.println("������ run �쳣: " + e.getMessage());    
        } finally {    
            if (socket != null) {    
                try {    
                    socket.close();    
                } catch (Exception e) {    
                    socket = null;    
                    System.out.println("����� finally �쳣:" + e.getMessage());    
                }    
            }    
        }   
    } 
}
