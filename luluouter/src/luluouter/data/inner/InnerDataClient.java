package luluouter.data.inner;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class InnerDataClient {
    private Socket inner;

    public InnerDataClient(Socket inner) {
        this.inner = inner;
    }

    public void init() {
        try {
            InputStream inputStream = inner.getInputStream();
            byte[] bytes = new byte[8];
            inputStream.read(bytes);
            long result = 0;
            for (int i = 0; i < 8; i++) {
                result <<= 8;
                result |= (bytes[i] & 0xFF);
            }
            InnerDataMaster.getInstance().addInnerDataSocket(result, inner);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
