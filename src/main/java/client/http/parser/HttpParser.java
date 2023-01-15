package client.http.parser;

import lombok.Data;

@Data
public class HttpParser {
    private final RequestParser requestParser = new RequestParser();
    private final ResponseParser responseParser = new ResponseParser();
}