package client.http.model.response;

import client.http.model.HttpHeaders;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class HttpResponse {

    private final int statusCode;
    private final HttpHeaders headers;
    private final String body;

    public JsonNode convertBodyToJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(body, JsonNode.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Response body does not contain JSON object", e);
        }
    }
}
