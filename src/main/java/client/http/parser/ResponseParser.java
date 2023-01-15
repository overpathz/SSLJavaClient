package client.http.parser;

import client.http.socketsender.Constants;
import client.http.util.JsonEntityLinePredicate;
import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

@Data
public class ResponseParser {

    private String body;
    private int statusCode;
    private Map<String, String> headers = new HashMap<>();
    private String response;
    private List<String> lines;

    public void parseResponse(String response) {
        this.lines = Arrays.asList(response.split("\n"));
        parseStatusCode();
        parseHeaders();
        parseBody();
    }

    private void parseBody() {
        body = lines.stream()
                .dropWhile(line -> !line.isEmpty())
                .skip(1)
                .filter(new JsonEntityLinePredicate())
                .collect(joining());
    }

    private void parseHeaders() {
        lines.stream()
                .skip(1)
                .takeWhile(line -> !line.isEmpty())
                .forEach(line -> {
                    int colonIndex = line.indexOf(Constants.COLON);
                    String key = line.substring(0, colonIndex);
                    String value = line.substring(colonIndex + 2);
                    headers.put(key, value);
                });
    }

    private void parseStatusCode() {
        setStatusCode(Integer.parseInt(lines.get(0).split(" ")[1]));
    }
}
