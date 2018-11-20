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
            // 读取客户端数据    
              input = new DataInputStream(socket.getInputStream());  
            while(true)
            {
                byte[] b = new byte[1024];
				int number = input.read(b );//这里要注意和客户端输出流的写方法对应,否则会抛 EOFException  
                // 处理客户端数据    
                 
            }
              
        } catch (Exception e) {    
            System.out.println("服务器 run 异常: " + e.getMessage());    
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
                    System.out.println("服务端 finally 异常:" + e.getMessage());    
                }    
            }      
        }   
    }   
}
