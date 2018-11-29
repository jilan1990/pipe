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
import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.fastjson.JSON;

import luluouter.data.inner.InnerDataMaster;
import luluouter.msg.outer.ProxyServerMaster;

public class InnerMsgClient {
    private final static int FLAG_MSG_GET_PORT = 1024;
    private final static int FLAG_MSG_CREATE_DATA_PIPE = 2048;

    private BlockingQueue<Map<String, Object>> outQueue = new LinkedBlockingQueue<Map<String, Object>>();

    private Socket inner;
    private String key;
    AtomicLong index = new AtomicLong();

    private volatile boolean stop = true;

    public InnerMsgClient(Socket inner) {
        this.inner = inner;
        key = String.valueOf(inner.getRemoteSocketAddress());
        stop = false;
        System.out.println("InnerMsgClient:" + key);
    }

    public void init() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.submit(() -> {
            try (OutputStream outputStream = inner.getOutputStream();
                    DataOutputStream out = new DataOutputStream(outputStream);
                    InputStream inputStream = inner.getInputStream();
                    DataInputStream in = new DataInputStream(inputStream);) {
                while (!stop) {
                    Map<String, Object> msg = outQueue.take();
                    System.out.println("InnerMsgClient.take:" + msg);
                    String json = JSON.toJSONString(msg);
                    out.writeUTF(json);
                    out.flush();
                    System.out.println("InnerMsgClient.writeUTF:" + json);

                    String line = in.readUTF();
                    Map<String, Object> result = JSON.parseObject(line, Map.class);
                    System.out.println("InnerMsgClient.readUTF:" + result);
                    dealMsg(result);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                System.out.println("InnerMsgClient.finally:" + key);
            }
        });
        executor.shutdown();

        Map<String, Object> msg = new HashMap<String, Object>();
        msg.put("msgType", FLAG_MSG_GET_PORT);
        outQueue.offer(msg);
        System.out.println("InnerMsgClient.init:" + msg);
    }

    private void dealMsg(Map<String, Object> msg) {
        Integer msgType = (Integer) msg.get("msgType");
        if (msgType == FLAG_MSG_GET_PORT) {
            int proxyPort = (Integer) msg.get("proxyPort");
            ProxyServerMaster.getInstance().addInnerClient(proxyPort, this);
        } else if (msgType == FLAG_MSG_CREATE_DATA_PIPE) {
            Object indexObj = msg.get("index");
            String indexStr = String.valueOf(indexObj);
            Long index = Long.parseLong(indexStr);

            String result = (String) msg.get("result");
            if ("success".equals(result)) {
                InnerDataMaster.getInstance().startPipes(index);
            } else {
                InnerDataMaster.getInstance().destroyOuterClient(index);
            }
        }
    }

    public void createPipes(Socket outerClient) {
        long theIndex = index.incrementAndGet();

        Map<String, Object> msg = new HashMap<String, Object>();
        msg.put("msgType", FLAG_MSG_CREATE_DATA_PIPE);
        msg.put("index", theIndex);
        outQueue.offer(msg);

        InnerDataMaster.getInstance().addOuterClient(theIndex, outerClient);
    }

    public String getKey() {
        return key;
    }

}
