package luluinner.upper;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import luluinner.util.Constants;

public class UpperAgentMaster {


    private static final UpperAgentMaster INSTANCE = new UpperAgentMaster();

    private UpperAgentMaster() {
    }

    public static UpperAgentMaster getInstance() {
        return INSTANCE;
    }

    public void init() {
        try {
            Socket socket = new Socket(Constants.OUTER_HOST, Constants.MSG_PORT);
            UpperAgentHandler agent = new UpperAgentHandler(socket);
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(agent);
            executor.shutdown();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
