package luluouter.msg.inner;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;

import com.alibaba.fastjson.JSON;

import luluouter.data.inner.InnerDataClient;
import luluouter.data.inner.InnerDataMaster;
import luluouter.msg.outer.ProxyServerMaster;

public class InnerMsgClient extends InnerClient {

    private BlockingQueue<Map<String, Object>> inQueue = new LinkedBlockingQueue<Map<String, Object>>();
    private BlockingQueue<Map<String, Object>> outQueue = new LinkedBlockingQueue<Map<String, Object>>();

    private Socket inner;
    private String key;
    private long index = 0;

    private volatile boolean stop = true;

    public InnerMsgClient(Socket inner) {
        this.inner = inner;
        key = String.valueOf(inner.getRemoteSocketAddress());
        System.out.println("InnerMsgClient:" + key);
    }

    @Override
    public void init() {
        executeOut();
        executeIn();
        try {
            Map<String, Object> msg = inQueue.take();
            int proxyPort = (Integer) msg.get("proxyPort");
            ProxyServerMaster.getInstance().addInnerClient(proxyPort, this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void newSocket(int proxyPort) {
        Map<String, Object> msg = new HashMap<String, Object>();
        msg.put("proxyPort", proxyPort);
        msg.put("index", index);
        outQueue.offer(msg);
        try {
            Map<String, Object> result = inQueue.take();
            System.out.println("InnerMsgClient.newSocket:" + result);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        InnerDataClient innerDataClient = InnerDataMaster.getInstance().getInnerDataClient(msg);
        index++;
    }

    private void executeIn() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.submit(() -> {
            try (InputStream inputStream = inner.getInputStream();
                    DataInputStream in = new DataInputStream(inputStream);) {

                String line = null;
                while ((line = in.readUTF()) != null) {
                    Map<String, Object> msg = JSON.parseObject(line, Map.class);
                    inQueue.offer(msg);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        executor.shutdown();
    }

    private void executeOut() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.submit(() -> {
            try (OutputStream outputStream = inner.getOutputStream();
                    DataOutputStream out = new DataOutputStream(outputStream);) {
                while (!stop) {
                    Map<String, Object> msg = outQueue.take();
                    String json = JSON.toJSONString(msg);
                    out.writeUTF(json);
                    out.flush();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        executor.shutdown();
    }

    public String getKey() {
        return key;
    }

}
