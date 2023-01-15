package client.http.sendstrategy;

import client.http.converter.Converter;
import client.http.model.HttpHeaders;
import client.http.model.request.HttpRequest;
import client.http.model.response.HttpResponse;
import client.http.parser.HttpParser;
import client.http.parser.ResponseParser;
import client.http.socketsender.Communicator;
import client.http.util.Utils;

public class HeadRequestSendStrategy extends BaseStrategy {

    public HeadRequestSendStrategy(Communicator communicator, HttpParser httpParser, Converter convertor) {
        super(communicator, httpParser, convertor);
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        setHostHeader(request);
        String stringHttpRequest = getConverter().convertRequestObjToString(request);
        String uri = request.getUri();
        String rawResponse = getCommunicator().send(stringHttpRequest.getBytes(), uri);
        parseResponse(rawResponse);
        ResponseParser respParser = getHttpParser().getResponseParser();
        return HttpResponse.builder()
                .body(respParser.getBody())
                .statusCode(respParser.getStatusCode())
                .headers(HttpHeaders.builder().map(respParser.getHeaders()).build())
                .build();
    }


    private void setHostHeader(HttpRequest request) {
        request.getHeaders().getMap().put("Host", Utils.getHostByURI(request.getUri()));
    }

    private void parseResponse(String rawResponse) {
        getHttpParser().getResponseParser().parseResponse(rawResponse);
    }
}
