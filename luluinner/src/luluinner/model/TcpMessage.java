package luluinner.model;

public class TcpMessage {
    public final static int IP_LENGTH = 4;
    public final static int PORT_LENGTH = 4;
    public final static int TYPE_LENGTH = 4;
    public final static int LENGTH_LENGTH = 4;
    public TcpMessage(byte[] bytes) {
        int pos = 0;
        byte[] ip = new byte[IP_LENGTH];
        System.arraycopy(bytes, pos, ip, 0, IP_LENGTH);
        pos += IP_LENGTH;

        int port = 0;
        for (int i = pos + PORT_LENGTH - 1; i >= pos; i--) {
            port <<= 8;
            port += 0xff & bytes[i];
        }
        pos += PORT_LENGTH;
        client = new Client(ip, port);

        type = 0;
        for (int i = pos + TYPE_LENGTH - 1; i >= pos; i--) {
            type <<= 8;
            type += 0xff & bytes[i];
        }
        pos += TYPE_LENGTH;

        length = 0;
        for (int i = pos + LENGTH_LENGTH - 1; i >= pos; i--) {
            length <<= 8;
            length += 0xff & bytes[i];
        }
        pos += LENGTH_LENGTH;

        content = new byte[length];
        System.arraycopy(bytes, pos, content, 0, length);
    }

    public TcpMessage(Client client, byte[] content, int length, int type) {
        this.client = client;
        this.type = type;

        length = length > 0 ? length : 0;
        this.length = length;

        this.content = new byte[length];
        System.arraycopy(content, 0, this.content, 0, length);
    }

    public Client getClient() {
        return client;
    }

    public byte[] getContent() {
        return content;
    }

    public int getLength() {
        return length;
    }

    public int getType() {
        return type;
    }

    private Client client;
    private byte[] content;
    private int length;
    private int type;

    public byte[] getBytes() {
        byte[] result = new byte[IP_LENGTH + PORT_LENGTH + TYPE_LENGTH + LENGTH_LENGTH + length];

        int pos = 0;
        byte[] ip = client.getIp();
        System.arraycopy(ip, 0, result, pos, IP_LENGTH);
        pos += IP_LENGTH;

        int port = client.getPort();
        for (int i = 0; i < PORT_LENGTH; i++) {
            result[pos + i] = (byte) (0xff & port);
            port >>= 8;
        }
        pos += PORT_LENGTH;

        int type = this.type;
        for (int i = 0; i < TYPE_LENGTH; i++) {
            result[pos + i] = (byte) (0xff & type);
            type >>= 8;
        }
        pos += TYPE_LENGTH;

        int length = this.length;
        for (int i = 0; i < LENGTH_LENGTH; i++) {
            result[pos + i] = (byte) (0xff & length);
            length >>= 8;
        }
        pos += LENGTH_LENGTH;

        System.arraycopy(content, 0, result, pos, length);
        return result;
    }

    @Override
    public String toString() {
        return "TcpMessage [client=" + client + ", length=" + length + ", type=" + type + "]";
    }
}
