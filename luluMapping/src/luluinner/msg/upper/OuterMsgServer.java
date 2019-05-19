package luluinner.msg.upper;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import luluinner.pipe.Pipe;
import luluinner.util.SocketUtil;

public class OuterMsgServer implements Runnable {
    private final static int FLAG_MSG_GET_PORT = 1024;
    private final static int FLAG_MSG_CREATE_DATA_PIPE = 2048;
    private final static String MSG_KEY_HEART_BEAT = "heartBeat";

    private Socket socket;
    private Map<String, Object> configs;

    private int mappingport;
    private String shyip;
    private int shyport;

    public OuterMsgServer(int mappingport, String shyip, int shyport) {
        this.mappingport = mappingport;
        this.shyip = shyip;
        this.shyport = shyport;
        System.out.println("OuterMsgServer: mappingport:" + mappingport + "->shyport:" + shyport);
    }

    @Override
    public void run() {

        try (ServerSocket serverSocket = new ServerSocket(mappingport);) {
            System.out.println("InnerMsgServer.listening:" + mappingport);
            while (true) {
                //
                Socket client = serverSocket.accept();
                //

                try {
                    System.out.println("OuterMsgServer.dealMsg.innerSocket:" + client.getRemoteSocketAddress());

                    Socket outerSocket = new Socket(shyip, shyport);
                    System.out.println("OuterMsgServer.dealMsg.outerSocket:" + outerSocket.getRemoteSocketAddress());

                    startPipes(outerSocket, client);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println("InnerServer.init: " + e.getMessage());
        }
    }


    private void startPipes(Socket outerSocket, Socket innerSocket) {
        try {
            startPipe(
                    new Pipe(outerSocket.getInputStream(), innerSocket.getOutputStream(), "outerSocket->innerSocket"));
            startPipe(
                    new Pipe(innerSocket.getInputStream(), outerSocket.getOutputStream(), "innerSocket->outerSocket"));
        } catch (IOException e) {
            e.printStackTrace();
            SocketUtil.close(outerSocket);
            SocketUtil.close(innerSocket);
        }
    }

    private void startPipe(Pipe pipe) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(pipe);
        executor.shutdown();
    }

}
