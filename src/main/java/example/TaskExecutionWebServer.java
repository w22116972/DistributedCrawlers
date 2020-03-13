package example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TaskExecutionWebServer {

    private static final int THREADS_NUMBER = 100;
    private static final Executor exec = Executors.newFixedThreadPool(THREADS_NUMBER);

    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (true) {
            final Socket connection = socket.accept();
            Runnable task = () -> {
                // handle request
            };
            exec.execute(task);
        }
    }
}
