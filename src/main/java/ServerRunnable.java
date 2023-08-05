import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

public class ServerRunnable extends Thread {

    public static Runnable setOut(BufferedReader in, BufferedOutputStream out, List<String> validPaths) {
        try {
            while (true) {
                final var requestLine = in.readLine();
                final var parts = requestLine.split(" ");
                if (parts.length != 3) {
                    continue;
                }

                final var pathWithQuery = parts[1];
                int queryStartIndex = pathWithQuery.indexOf('?');
                final var path = (queryStartIndex != -1) ? pathWithQuery.substring(0, queryStartIndex) : pathWithQuery;
                Request request = new Request(path);

                if (queryStartIndex != -1) {
                    String queryString = pathWithQuery.substring(queryStartIndex + 1);
                    request.setQueryString(queryString);
                }
                System.out.println(request.getQueryParams());
                System.out.println("Login: " + request.getQueryParam("login"));
                System.out.println("Password: " + request.getQueryParam("password"));

                if (!validPaths.contains(request.getPath())) {
                    out.write((
                            "HTTP/1.1 404 Not Found\r\n" +
                                    "Content-Length: 0\r\n" +
                                    "Connection: close\r\n" +
                                    "\r\n"
                    ).getBytes());
                    out.flush();
                    continue;
                }

                final var filePath = Path.of(".", "public", request.getPath());
                final var mimeType = Files.probeContentType(filePath);

                if (request.getPath().equals("/classic.html")) {
                    final var template = Files.readString(filePath);
                    final var content = template.replace(
                            "{time}",
                            LocalDateTime.now().toString()
                    ).getBytes();
                    out.write((
                            "HTTP/1.1 200 OK\r\n" +
                                    "Content-Type: " + mimeType + "\r\n" +
                                    "Content-Length: " + content.length + "\r\n" +
                                    "Connection: close\r\n" +
                                    "\r\n"
                    ).getBytes());
                    out.write(content);
                    out.flush();
                    continue;
                }

                final var length = Files.size(filePath);
                out.write((
                        "HTTP/1.1 200 OK\r\n" +
                                "Content-Type: " + mimeType + "\r\n" +
                                "Content-Length: " + length + "\r\n" +
                                "Connection: close\r\n" +
                                "\r\n"
                ).getBytes());
                Files.copy(filePath, out);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
