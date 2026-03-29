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
    public static final int MAX_PATH_INDEX = 2;
    public static final int URL_PATH_INDEX = 1;
    public static final int MAX_LENGTH_INDEX = 2;
    private static final int URL_LENGTH_INDEX = 1;

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

    public static String extractPath(String line) {
        String[] tokens = line.split(" ");
        if (tokens.length < MAX_PATH_INDEX) {
            throw new IllegalArgumentException("Invalid Request Line: " + line);
        }
        return tokens[URL_PATH_INDEX];
    }

    public static int extractLength(String line) {
        String[] tokens = line.split(":");
        if (tokens.length < MAX_LENGTH_INDEX) {
            throw new IllegalArgumentException("Invalid Request Line: " + line);
        }
        return Integer.parseInt(tokens[URL_LENGTH_INDEX].trim());
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