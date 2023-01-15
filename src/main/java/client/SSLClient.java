package client;

import client.http.model.HttpHeaders;
import client.http.model.HttpMethod;
import client.http.model.request.HttpRequest;
import client.http.model.response.HttpResponse;
import client.http.sendstrategy.MethodStrategyWrapper;
import client.http.socketsender.Constants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.util.*;

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
    public static void main(String[] args) throws IOException, InterruptedException {

        SSLClient client = new SSLClient();

        HttpRequest build = HttpRequest.builder().uri(Constants.FULL_API_NASA_URI)
                .httpMethod(HttpMethod.GET)
                .headers(HttpHeaders.builder().map(new HashMap<>()).build())
                .build();

        HttpResponse allPhotosResponse = client.send(build);
        String allPhotosRespJson = allPhotosResponse.getBody();

        JsonNode rootJson = new ObjectMapper().readValue(allPhotosRespJson, JsonNode.class);
        List<String> imgSrcRedirectLinks = new ArrayList<>();
        rootJson.get("photos").spliterator().forEachRemaining(x -> imgSrcRedirectLinks.add(x.get("img_src").asText()));

        List<String> realLinks = new ArrayList<>();

        imgSrcRedirectLinks.forEach(imgLink -> {
            HttpResponse send = client.send(HttpRequest.builder()
                    .httpMethod(HttpMethod.HEAD)
                    .uri(imgLink)
                    .headers(HttpHeaders.builder()
                            .map(new HashMap<>())
                            .build()).build());
            realLinks.add(send.getHeaders().location());
            System.out.println(realLinks.size());
        });

        Map<String, Integer> imgLinkContentLengthMap = new HashMap<>();

        realLinks.forEach(realLink -> {
            HttpResponse send = client.send(HttpRequest.builder()
                    .httpMethod(HttpMethod.HEAD)
                    .uri(realLink)
                    .headers(HttpHeaders.builder()
                            .map(new HashMap<>())
                            .build()).build());
            imgLinkContentLengthMap.put(realLink, Integer.parseInt(send.getHeaders().contentLength()));
        });

        String largestPic = Collections.max(imgLinkContentLengthMap.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
        System.out.println("Largest picture: " + largestPic);
        System.out.println("Size: " + imgLinkContentLengthMap.get(largestPic));
    }
}
