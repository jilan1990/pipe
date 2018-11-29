package luluinner.upper;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import luluinner.msg.upper.OuterMsgServer;

public class UpperAgentMaster {


    private static final UpperAgentMaster INSTANCE = new UpperAgentMaster();

    private UpperAgentMaster() {
    }

    public static UpperAgentMaster getInstance() {
        return INSTANCE;
    }

    public void init(Map<String, Object> configs) {

        String outer_ip = (String) configs.get("outer_ip");
        int outer_msg_port = (Integer) configs.get("outer_msg_port");

        try {
            Socket socket = new Socket(outer_ip, outer_msg_port);
            System.out.println("UpperAgentMaster.init" + socket.getRemoteSocketAddress());
            OuterMsgServer outerServer = new OuterMsgServer(socket, configs);

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(outerServer);
            executor.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
