package client.http.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class HttpHeaders {

    private final Map<String, String> map;

    private String getHeader(String header) {
        return map.get(header);
    }

    public String location() {
        return map.get(Headers.Location.name());
    }

    public String contentType() {
        return map.get(Headers.ContentType.name());
    }

    public String contentLength() {
        return map.get(Headers.ContentLength.name());
    }

    private enum Headers {
        Location("Location"),
        AcceptType("Accept"),
        ContentType("Content-Type"),
        ContentLength("Content-Length");

        Headers(String header) {
        }
    }
}
