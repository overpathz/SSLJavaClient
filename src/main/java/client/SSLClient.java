package client;

import client.http.model.request.HttpRequest;
import client.http.model.response.HttpResponse;
import client.http.sendstrategy.MethodStrategyWrapper;

public class SSLClient {

    private final MethodStrategyWrapper strategyWrapper = new MethodStrategyWrapper();

    public HttpResponse send(HttpRequest request) {
        return chooseStrategyAndProcess(request);
    }

    private HttpResponse chooseStrategyAndProcess(HttpRequest request) {
        return strategyWrapper
                .getStrategy(request.getHttpMethod())
                .get()
                .handle(request);
    }
}
