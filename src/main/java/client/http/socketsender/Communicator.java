package client.http.socketsender;

import client.http.util.Utils;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class Communicator {

    private static final SSLSocketFactory SSL_SOCKET_FACTORY = (SSLSocketFactory) SSLSocketFactory.getDefault();

    public String send(byte[] data, String uri) {
        int port = Utils.getPortByURI(uri);
        String host = Utils.getHostByURI(uri);
        return sendAndRetrieveResponse(data, port, host);
    }

    private String sendAndRetrieveResponse(byte[] data, int port, String host) {
        StringBuilder responseBuilder = new StringBuilder();
        try(SSLSocket socket = getSocket(host, port);
            OutputStream outputStream = socket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            outputStream.write(data);
            outputStream.flush();

            String line;
            while ((line=reader.readLine()) != null) {
                if (line.equals("0")) break;
                responseBuilder.append(line).append("\n");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
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
