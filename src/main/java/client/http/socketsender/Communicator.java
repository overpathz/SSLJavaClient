package client.http.socketsender;

import client.http.util.Utils;
import lombok.SneakyThrows;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class Communicator {

    private static final SSLSocketFactory SSL_SOCKET_FACTORY = (SSLSocketFactory) SSLSocketFactory.getDefault();

    public String send(String data, String uri, String method) {
        int port = Utils.getPortByURI(uri);
        String host = Utils.getHostByURI(uri);
        return sendAndRetrieveResponse(data, port, host, method);
    }

    @SneakyThrows
    private String sendAndRetrieveResponse(String data, int port, String host, String method) {
        StringBuilder responseBuilder = new StringBuilder();
        try(SSLSocket socket = getSocket(host, port);
            OutputStream outputStream = socket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            outputStream.write(data.getBytes());
            outputStream.flush();

            int emptyCount = 0;
            int emptyLinesThreshold = method.equals("GET") ? 1 : 0;
            String line;
            while ((line=reader.readLine()) != null) {
                if (line.isEmpty()) {
                    if (++emptyCount > emptyLinesThreshold) break;
                }
                responseBuilder.append(line).append("\n");
            }

        }
        return responseBuilder.toString();
    }

    private SSLSocket getSocket(String host, int port) {
        try {
            return (SSLSocket) SSL_SOCKET_FACTORY.createSocket(host, port);
        } catch (IOException e) {
            throw new RuntimeException("Cannot create a socket with host " + host +
                    " and " + port + " port");
        }
    }
}
