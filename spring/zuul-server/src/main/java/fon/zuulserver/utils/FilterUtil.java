package fon.zuulserver.utils;

import org.springframework.stereotype.Component;

@Component
public class FilterUtil {

    public static final String CORRELATION_ID = "tmx-correlation-id";
    public static final String SESSION_ID = "Cookie";
    public static final String PRE_FILTER_TYPE = "pre";
    public static final String POST_FILTER_TYPE = "post";
}
