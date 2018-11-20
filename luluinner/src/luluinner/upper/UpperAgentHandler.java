package luluinner.upper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import luluinner.model.TcpMessage;
import luluinner.service.ServiceAgent;
import luluinner.util.Constants;
import luluinner.util.SocketUtil;

public class UpperAgentHandler implements Runnable {
    private DataInputStream input = null;
    private DataOutputStream out = null;

    public UpperAgentHandler(Socket socket) {
        try {
            input = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("corrine");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                byte[] bytes = SocketUtil.receiveFromUpper(input);
                TcpMessage msg = new TcpMessage(bytes);
                ServiceAgent.getInstance().dealMsg(msg);
                TcpMessage result = new TcpMessage(msg.getClient(), new byte[0], 0, Constants.MSG_CONNECT);
                SocketUtil.send2Outer(out, result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(TcpMessage message) {
        try {
            SocketUtil.send2Outer(out, message);
        } catch (Exception e) {
            System.out.println("·þÎñÆ÷ run Òì³£: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
