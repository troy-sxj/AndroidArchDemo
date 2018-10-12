package com.mika.lib.net;

public class RestApi {

    public interface HostEnvironmentProtocol {
        String HTTPS = "https://";
        String HTTP = "http://";
    }

    interface Domain {
        String GankIo = "gank.io/";
    }

    public interface Url {
        //最新一天的干货
        String GankIOAPi = HostEnvironmentProtocol.HTTP + "gank.io/api/";
    }
}
