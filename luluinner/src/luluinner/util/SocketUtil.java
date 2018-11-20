package luluinner.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import luluinner.model.TcpMessage;


public class SocketUtil {
    public final static int HEAD_LENGTH = 4;

    public static byte[] receiveFromUpper(DataInputStream input) throws IOException {
        byte[] head = new byte[HEAD_LENGTH];
        input.read(head);
        int length = 0;
        for (int i = HEAD_LENGTH - 1; i >= 0; i--) {
            length <<= 8;
            length += 0xff & head[i];
        }
        byte[] body = new byte[length];
        input.read(body);
        return body;
    }

    public static void close(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception e) {
                socket = null;
                System.out.println("服务端 finally 异常:" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void close(InputStream input) {
        if (input != null) {
            try {
                input.close();
            } catch (Exception e) {
                input = null;
                System.out.println("服务端 finally 异常:" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void close(OutputStream out) {
        if (out != null) {
            try {
                out.close();
            } catch (Exception e) {
                out = null;
                System.out.println("服务端 finally 异常:" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void send2Outer(DataOutputStream out, TcpMessage message) throws IOException {
        byte[] msgBytes = message.getBytes();
        int length = msgBytes.length;
        byte[] bytes = new byte[HEAD_LENGTH + length];

        int pos = 0;
        for (int i = 0; i < HEAD_LENGTH; i++) {
            bytes[pos + i] = (byte) (0xff & length);
            length >>= 8;
        }
        pos += HEAD_LENGTH;

        System.arraycopy(msgBytes, 0, bytes, pos, msgBytes.length);
        out.write(bytes);
    }

    public static void send2Service(DataOutputStream out, TcpMessage message) throws IOException {
        byte[] content = message.getContent();
        out.write(content);
    }

    // public static byte[] receiveFromService(DataInputStream input) throws
    // IOException {
    //
    // byte[] body = new byte[BUFFER_LENGTH];
    // int length = input.read(body);
    //
    // byte[] head = new byte[HEAD_LENGTH];
    // input.read(head);
    // int length = 0;
    // for (int i = HEAD_LENGTH - 1; i >= 0; i--) {
    // length <<= 8;
    // length += 0xff & head[i];
    // }
    // byte[] body = new byte[length];
    // input.read(body);
    // return body;
    // }
}
