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
        return map.get(Headers.Location.value());
    }

    public String contentType() {
        return map.get(Headers.ContentType.value());
    }

    public String contentLength() {
        return map.get(Headers.ContentLength.value());
    }

    private enum Headers {

        Location("Location"),
        ContentType("Content-Type"),
        ContentLength("Content-Length");

        private final String header;

        Headers(String header) {
            this.header = header;
        }

        public String value() {
            return header;
        }
    }
}
