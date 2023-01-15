package client.http.converter;

import client.http.model.request.HttpRequest;
import client.http.util.Utils;

public class Converter {

    public String convertRequestObjToString(HttpRequest request) {
        String uri = request.getUri();
        String template = HttpTemplates.getTemplateByMethod(request.getHttpMethod());
        StringBuilder headers = new StringBuilder();
        request.getHeaders().getMap().forEach((key, value) -> headers.append(key)
                .append(':').append(' ')
                .append(value)
                .append('\n'));

        return template.formatted(Utils.getResourceByURI(uri), headers.toString());
    }
}
