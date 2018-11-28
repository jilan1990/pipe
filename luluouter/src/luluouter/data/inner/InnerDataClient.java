package luluouter.data.inner;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import luluouter.msg.inner.InnerClient;

public class InnerDataClient extends InnerClient {
    private Socket inner;

    public InnerDataClient(Socket inner) {
        this.inner = inner;
    }

    @Override
    public void init() {
        try {
            InputStream inputStream = inner.getInputStream();
            DataInputStream in = new DataInputStream(inputStream);
            String line = in.readUTF();
            Map<String, Object> result = JSON.parseObject(line, Map.class);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("proxyPort", result.get("proxyPort"));
            map.put("index", result.get("index"));
            InnerDataMaster.getInstance().addDataClient(map, this);

            OutputStream outputStream = inner.getOutputStream();
            DataOutputStream out = new DataOutputStream(outputStream);
            out.writeUTF(line);
            out.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
