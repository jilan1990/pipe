package luluouter.outer;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReaderThread  implements Runnable {
	private Socket socket;
    public ReaderThread(Socket client) {    
        socket = client;    
        new Thread(this).start();    
    }


    public void run() {     
        DataInputStream input =null;
        try {    
            // ��ȡ�ͻ�������    
              input = new DataInputStream(socket.getInputStream());  
            while(true)
            {
                byte[] b = new byte[1024];
				int number = input.read(b );//����Ҫע��Ϳͻ����������д������Ӧ,������� EOFException  
                // ����ͻ�������    
                 
            }
              
        } catch (Exception e) {    
            System.out.println("������ run �쳣: " + e.getMessage());    
        } finally { 
        	if(input != null)
        	{
                try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}  
        	}
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
