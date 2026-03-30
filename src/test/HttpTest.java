import org.junit.jupiter.api.Test;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTest {
    @Test
    public void HttpRequestTest() throws IOException {
        HttpRequest httpRequest = HttpRequest.from(bufferedReaderFromFile("./webapp" + "/user/create"));
        assertEquals("/user/create", httpRequest.getUrl());
    }

    @Test
    public void HttpResponseTest() throws IOException {
        HttpResponse httpResponse = new HttpResponse(outputStreamToFile("./webapp" + "/index.html"));
        httpResponse.forward("/index.html");

    }

    private BufferedReader bufferedReaderFromFile(String path) throws IOException {
        return new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(path))));
    }

    private DataOutputStream outputStreamToFile(String path) throws IOException {
        OutputStream fos = Files.newOutputStream(Paths.get(path));
        return new DataOutputStream(fos);
    }
}
