package luluouter.msg.inner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;

import com.alibaba.fastjson.JSON;

import luluouter.msg.outer.ProxyServerMaster;

public class InnerClient {
    private BlockingQueue<Map<String, Object>> inQueue = new LinkedBlockingQueue<Map<String, Object>>();
    private BlockingQueue<Map<String, Object>> outQueue = new LinkedBlockingQueue<Map<String, Object>>();

    private Socket inner;
    private boolean flag = true;

    public InnerClient(Socket inner) {
        this.inner = inner;
    }

    public void init() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.submit(() -> {
            executeOut();
            executeIn();
            try {
                Map<String, Object> msg = inQueue.take();
                int proxyPort = (Integer) msg.get("proxyPort");
                ProxyServerMaster.getInstance().addInnerClient(proxyPort, this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        executor.shutdown();
    }

    private void executeIn() {

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.submit(() -> {
            try (InputStream inputStream = inner.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader in = new BufferedReader(inputStreamReader);) {

                String line = null;
                while ((line = in.readLine()) != null) {
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
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                    BufferedWriter out = new BufferedWriter(outputStreamWriter);) {
                while (flag) {
                    Map<String, Object> msg = outQueue.take();
                    String json = JSON.toJSONString(msg);
                    out.write(json);
                    out.write(System.lineSeparator());
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

}
