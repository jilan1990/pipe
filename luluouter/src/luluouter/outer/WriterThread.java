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
            // 读取客户端数据    

            // 向客户端回复信息    
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());    
            System.out.print("请输入:\t");    
            // 发送键盘输入的一行    
            String s = new BufferedReader(new InputStreamReader(System.in)).readLine();    
            out.writeUTF(s);    
              
            out.close();    
        } catch (Exception e) {    
            System.out.println("服务器 run 异常: " + e.getMessage());    
        } finally {    
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
