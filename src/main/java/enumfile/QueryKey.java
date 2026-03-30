package enumfile;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public enum QueryKey {
    USERID("userId"),
    PASSWORD("password"),
    NAME("name"),
    EMAIL("email");

    private final String key;

    QueryKey(String key) {
        this.key = key;
    }

    public String safeDecode(Map<String, String> map) {
        String value = map.get(this.key);
        if (value == null) return "";
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
