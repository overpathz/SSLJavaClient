package client.http.sendstrategy;

import client.http.converter.Converter;
import client.http.model.HttpMethod;
import client.http.parser.HttpParser;
import client.http.socketsender.Communicator;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public class MethodStrategyWrapper {

    private final Map<HttpMethod, Supplier<SendStrategy>> methodStrategyMap = new EnumMap<>(HttpMethod.class);

    private final Communicator communicator = new Communicator();
    private final HttpParser parser = new HttpParser();
    private final Converter converter = new Converter();

    public MethodStrategyWrapper() {
        methodStrategyMap.put(HttpMethod.GET,
                () -> new GetRequestSendStrategy(communicator, parser, converter));

        methodStrategyMap.put(HttpMethod.HEAD,
                () -> new HeadRequestSendStrategy(communicator, parser, converter));
    }

    public Supplier<SendStrategy> getStrategy(HttpMethod httpMethod) {
        return methodStrategyMap.get(httpMethod);
    }
}
