import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static final int PORT = 9999;
    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = server.accept();
                try {
                    Server.executor.execute(new Server(socket));
                } catch (IOException e) {
                    socket.close();
                }
            }
        }
    }
}