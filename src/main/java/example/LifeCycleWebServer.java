package example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

public class LifeCycleWebServer {

    private static final int THREADS_NUMBER = 10;
    private final ExecutorService exec = Executors.newFixedThreadPool(THREADS_NUMBER);

    public void start() throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (!exec.isShutdown()) {
            try {
                final Socket conn = socket.accept();
                exec.execute(
                        () -> handleRequest(conn));
            } catch (RejectedExecutionException e) {
                if (!exec.isShutdown()) {
                    //log("task submission rejected", e);
                }
            }

        }

    }

    public void stop() {
        exec.shutdown();
    }

    void handleRequest(Socket connection) {
//        Request req = readRequest(connection);
//        if (isShutdownRequest(req)) {
//            stop();
//        } else dispatchRequest(req);
    }
}
