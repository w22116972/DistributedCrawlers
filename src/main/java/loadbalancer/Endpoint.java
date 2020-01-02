package loadbalancer;

public class Endpoint {

    public final String host;
    public final int port;
    public final int weight;
    private volatile boolean isOnLine = true;

    public Endpoint(String host, int port, int weight) {
        this.host = host;
        this.port = port;
        this.weight = weight;
    }

    public boolean isOnLine() {
        return isOnLine;
    }

    public void setOnLine(boolean onLine) {
        isOnLine = onLine;
    }

}
