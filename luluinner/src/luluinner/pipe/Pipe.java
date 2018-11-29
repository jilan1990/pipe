package luluinner.pipe;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import luluinner.util.Constants;
import luluinner.util.SocketUtil;

public class Pipe implements Runnable {
    private InputStream input;
    private OutputStream output;
    private String name;
    private volatile boolean flag = false;

    public Pipe(InputStream inputStream, OutputStream outputStream, String name) {
        this.input = inputStream;
        this.output = outputStream;
        this.name = name;
        flag = true;
        System.out.println(name);
    }

    @Override
    public void run() {
        byte[] buffer = new byte[Constants.BUFFER_LENGTH];
        try {
            while (flag) {
                int length = input.read(buffer);
                System.out.println("receive " + name + " length:" + length);
                if (length > 0) {
                    output.write(buffer, 0, length);
                    output.flush();
                    continue;
                }
                flag = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            SocketUtil.close(input);
            // SocketUtil.close(output);
        }
    }

}
