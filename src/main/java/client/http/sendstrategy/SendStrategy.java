package client.http.sendstrategy;

import client.http.model.request.HttpRequest;
import client.http.model.response.HttpResponse;

public interface SendStrategy {
    HttpResponse handle(HttpRequest request);
}
