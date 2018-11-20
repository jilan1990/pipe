package luluinner.service;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import luluinner.model.TcpMessage;
import luluinner.pipe.Pipe;
import luluinner.util.Constants;
import luluinner.util.SocketUtil;

public class ServiceAgent {
    public final static ServiceAgent INSTANCE = new ServiceAgent();

    // Map<Client, ServiceHandlerThread> handlers = new
    // ConcurrentHashMap<Client, ServiceHandlerThread>();

    private ServiceAgent() {

    }

    public static ServiceAgent getInstance() {
        return INSTANCE;
    }

    public void dealMsg(TcpMessage msg) {
        Socket service = null;
        Socket upper = null;
        try {
            service = new Socket(Constants.SERVICE_HOST, Constants.SERVICE_PORT);
            upper = new Socket(Constants.OUTER_HOST, Constants.PIPE_PORT);
        } catch (IOException e) {
            e.printStackTrace();
            SocketUtil.close(service);
            SocketUtil.close(upper);
            return;
        }

        try {
            startPipe(new Pipe(service.getInputStream(), upper.getOutputStream()));
            startPipe(new Pipe(upper.getInputStream(), service.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            SocketUtil.close(service);
            SocketUtil.close(upper);
        }
        // Client client = msg.getClient();
        // ServiceHandlerThread handler = handlers.get(client);
        // if (handler == null) {
        // handler = new ServiceHandlerThread(client);
        //
        // ExecutorService executor = Executors.newSingleThreadExecutor();
        // executor.submit(handler);
        // executor.shutdown();
        //
        // handlers.put(client, handler);
        // }
        // handler.dealMsg(msg);
    }

    private void startPipe(Pipe pipe) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(pipe);
        executor.shutdown();
    }

    // public void removeHandler(Client c) {
    // handlers.remove(c);
    // }
}
