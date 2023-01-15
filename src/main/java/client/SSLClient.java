package client;

import client.http.model.HttpHeaders;
import client.http.model.HttpMethod;
import client.http.model.request.HttpRequest;
import client.http.model.response.HttpResponse;
import client.http.sendstrategy.MethodStrategyWrapper;
import client.http.socketsender.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

public class SSLClient {

    private final MethodStrategyWrapper strategyWrapper = new MethodStrategyWrapper();

    private HttpResponse send(HttpRequest request) {
        return chooseStrategyAndProcess(request);
    }

    private HttpResponse chooseStrategyAndProcess(HttpRequest request) {
        return strategyWrapper
                .getStrategy(request.getHttpMethod())
                .get()
                .handle(request);
    }

    // test
    public static void main(String[] args) throws JsonProcessingException {

        SSLClient client = new SSLClient();

        HttpRequest build = HttpRequest.builder().uri(Constants.FULL_API_NASA_URI)
                .httpMethod(HttpMethod.GET)
                .headers(HttpHeaders.builder().map(new HashMap<>()).build())
                .build();

        HttpResponse allPhotosResponse = client.send(build);
        String allPhotosRespJson = allPhotosResponse.getBody();

        JsonNode rootJson = new ObjectMapper().readValue(allPhotosRespJson, JsonNode.class);
        List<String> imgLinks = StreamSupport.stream(rootJson.get("photos").spliterator(), false)
                .map(object -> object.get("img_src").asText())
                .toList();

        List<String> locations = new ArrayList<>();
        Map<String, Integer> imgSizeMap = new HashMap<>();

        imgLinks.stream().
                parallel().
                forEach(imgLink -> {
            HttpRequest req = HttpRequest.builder().uri(imgLink)
                    .httpMethod(HttpMethod.HEAD)
                    .headers(HttpHeaders.builder().map(new HashMap<>()).build())
                    .build();
            locations.add(client.send(req).getHeaders().location());
        });

        locations.stream().parallel().
        forEach(location -> {
            HttpRequest req = HttpRequest.builder().uri(location)
                    .httpMethod(HttpMethod.HEAD)
                    .headers(HttpHeaders.builder().map(new HashMap<>()).build())
                    .build();
            imgSizeMap.put(location, Integer.parseInt(client.send(req).getHeaders().contentLength()));
        });

        System.out.println(imgSizeMap);
    }
}
