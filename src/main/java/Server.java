import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server extends ServerRunnable {

    private final BufferedReader in;
    private final BufferedOutputStream out;
    protected static final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(64);
    final List<String> validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");

    public Server(Socket socket) throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            executor.execute(Objects.requireNonNull(setOut(in, out, validPaths)));
        } catch (Exception ignored) {}
    }
}