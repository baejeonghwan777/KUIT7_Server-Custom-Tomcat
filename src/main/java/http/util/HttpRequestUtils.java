package http.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequestUtils {

    public static Map<String, String> parseQueryParameter(String queryString) {
        try {
            String[] queryStrings = queryString.split("&");

            return Arrays.stream(queryStrings)
                    .map(q -> q.split("="))
                    .collect(Collectors.toMap(queries -> queries[0], queries -> queries[1]));
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    public static byte[] readPath(String path, String url) throws IOException {
        try {
            return Files.readAllBytes(new File(path + url).toPath());
        } catch (NoSuchFileException e) {
            return "File Not Found".getBytes();
        } catch (IOException e) {
            return "Error occurred".getBytes();
        }
    }
}