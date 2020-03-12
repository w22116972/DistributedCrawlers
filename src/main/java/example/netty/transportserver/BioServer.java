package example.netty.transportserver;

import scala.concurrent.impl.FutureConvertersImpl;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class BioServer {

    public void serve(int port) throws IOException {
        // bind server to specific port
        final ServerSocket socket = new ServerSocket(port);
        try {
            for (;;) {
                // accept a connection
                final Socket clientSocket = socket.accept();
                // create a new thread to handle connection
                new Thread(() -> {
                    OutputStream out;
                    try {
                        out = clientSocket.getOutputStream();
                        // send Hi message to connected client
                        out.write("Hi\n".getBytes(Charset.forName("UTF-8")));
                        out.flush();
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        try {
                            clientSocket.close();
                        } catch (IOException ex) {
                            // ignore on close
                        }
                    }
                }).start();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
