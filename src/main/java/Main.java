import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    public static ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(64);
    public static final int PORT = 9999;
    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = server.accept();
                try {
                    executor.execute(new Server(socket));
                    //serverList.add(new Server(socket));
                } catch (IOException e) {
                    socket.close();
                }
            }
        }
    }
}