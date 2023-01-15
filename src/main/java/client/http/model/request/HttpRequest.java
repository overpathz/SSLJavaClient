package client.http.model.request;

import client.http.model.HttpHeaders;
import client.http.model.HttpMethod;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HttpRequest {
    private final HttpMethod httpMethod;
    private final HttpHeaders headers;
    private final String uri;
}
