package luluinner.model;

import java.util.Arrays;

public class Client {
    private byte[] ip = null;
    private int port;

    public Client(byte[] ip, int port) {
        super();
        this.ip = ip;
        this.port = port;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(ip);
        result = prime * result + port;
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Client other = (Client) obj;
        if (!Arrays.equals(ip, other.ip))
            return false;
        if (port != other.port)
            return false;
        return true;
    }

    public byte[] getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "Client [ip=" + Arrays.toString(ip) + ", port=" + port + "]";
    }
}
