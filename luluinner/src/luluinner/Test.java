package luluinner;

import luluinner.model.Client;
import luluinner.model.TcpMessage;

public class Test {

    public static void main(String[] args) {
        String strIp = "180.102.112.89";
        byte[] ip = strIp2byteIp(strIp);
        
        byte[] content = new byte[1024];
        content[0] = 10;
        content[1] = 11;
        content[2] = 12;
        content[3] = 13;
        content[4] = 14;
        Client c = new Client(ip, 48479);
        TcpMessage msg = new TcpMessage(c, content, 5, 1);
        System.out.println(msg);

        TcpMessage msg2 = new TcpMessage(msg.getBytes());
        System.out.println(msg2);
    }

    private static byte[] strIp2byteIp(String strIp) {
        byte[] result = new byte[4];
        String[] ss = strIp.split("\\.");
        int i = 0;
        for (String s : ss) {
            result[i++] = (byte) (0xff & Integer.parseInt(s));
        }
        return result;
    }
}
