package client.http.sendstrategy;

import client.http.converter.Converter;
import client.http.parser.HttpParser;
import client.http.socketsender.Communicator;
import lombok.Data;

@Data
public abstract class BaseStrategy implements SendStrategy {
    private final Communicator communicator;
    private final HttpParser httpParser;
    private final Converter converter;
}
