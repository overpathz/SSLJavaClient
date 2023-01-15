package client.http.converter;

import client.http.model.HttpMethod;

import java.util.EnumMap;
import java.util.Map;

public final class HttpTemplates {

    private HttpTemplates() {
        super();
    }

    private static final String GET = """
            GET %s HTTP/1.1
            %s
            
            """;

    private static final String HEAD = """
            HEAD %s HTTP/1.1
            %s
            
            """;

    private static final Map<HttpMethod, String> methodTemplateMap = new EnumMap<>(HttpMethod.class);

    static {
        methodTemplateMap.put(HttpMethod.GET, GET);
        methodTemplateMap.put(HttpMethod.HEAD, HEAD);
    }

    public static String getTemplateByMethod(HttpMethod method) {
        return methodTemplateMap.get(method);
    }
}