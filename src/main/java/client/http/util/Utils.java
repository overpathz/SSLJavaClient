package client.http.util;

import client.http.socketsender.Constants;

public final class Utils {

    private Utils() {
        super();
    }

    public static int getPortByURI(String uri) {
        return uri.split(Constants.PROTOCOL_SEP)[0].contains("s") ? Constants.HTTPS_PORT : Constants.HTTP_PORT;
    }

    public static String getHostByURI(String uri) {
        return uri.split(Constants.PROTOCOL_SEP)[1].split(Constants.SLASH)[0];
    }

    public static String getResourceByURI(String uri) {
        String resourceWithHost = uri.split(Constants.PROTOCOL_SEP)[1];
        int index = resourceWithHost.indexOf(Constants.SLASH);
        return resourceWithHost.substring(index);
    }
}
