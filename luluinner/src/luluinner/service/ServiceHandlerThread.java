package luluinner.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import luluinner.MessageMaster;
import luluinner.model.Client;
import luluinner.model.TcpMessage;
import luluinner.util.Constants;
import luluinner.util.SocketUtil;

public class ServiceHandlerThread implements Runnable {
    public final static int BUFFER_LENGTH = 1412;

    private Client client = null;
    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream out = null;
    private boolean flag = false;

    public ServiceHandlerThread(Client client) {
        try {
            this.client = client;
            socket = new Socket(Constants.SERVICE_HOST, Constants.SERVICE_PORT);
            input = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dealMsg(TcpMessage msg) {
        int type = msg.getType();
        switch (type) {
        case Constants.MSG_CONTENT:
            send(msg);
            break;
        case Constants.MSG_BREAK:
            flag = false;
            SocketUtil.close(out);
            SocketUtil.close(input);
            SocketUtil.close(socket);
            // ServiceAgent.getInstance().removeHandler(client);
            break;
        }
    }

    public void send(TcpMessage msg) {
        try {
            System.out.println("send:" + msg);
            SocketUtil.send2Service(out, msg);
        } catch (Exception e) {
            System.out.println("服务器 run 异常: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (flag) {
                int type = Constants.MSG_CONTENT;
                byte[] body = new byte[BUFFER_LENGTH];
                int length = input.read(body);
                System.out.println("ServiceHandlerThread length:" + length);
                if (length == 0 || length == -1) {
                    continue;
                }
                TcpMessage msg = new TcpMessage(client, body, length, type);
                System.out.println("ServiceHandlerThread:" + msg);
                MessageMaster.getInstance().addMessage(msg);
            }
        } catch (Exception e) {
            System.out.println("服务器 run 异常: " + e.getMessage());
            e.printStackTrace();
        } finally {
            SocketUtil.close(out);
            SocketUtil.close(input);
            SocketUtil.close(socket);
        }
    }

}
