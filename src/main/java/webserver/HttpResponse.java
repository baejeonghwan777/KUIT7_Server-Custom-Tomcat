package webserver;

import enumfile.HttpHeader;
import http.util.HttpRequestUtils;
import enumfile.URL;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HttpResponse {
    private static final Logger log = Logger.getLogger(RequestHandler.class.getName());
    private final DataOutputStream dos;
    private final Map<String, String> headers = new HashMap<>();

    public HttpResponse(DataOutputStream dos) {
        this.dos = dos;
    }

    public void forward(String url) {
        try {
            byte[] body = HttpRequestUtils.readPath("./webapp", url);

            if (url.endsWith(".css")) addHeader(HttpHeader.CONTENT_TYPE.getHeader(), URL.CSS.getLink());
            if (!url.endsWith(".css")) addHeader(HttpHeader.CONTENT_TYPE.getHeader(), URL.HTML.getLink());

            response200Header(body.length);
            responseBody(body);
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    public void sendRedirect(String redirectUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + redirectUrl + "\r\n");
            processHeaders();
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    // 공통 헤더 추가
    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    private void response200Header(int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            processHeaders();
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    private void processHeaders() throws IOException {
        for (String key : headers.keySet()) {
            dos.writeBytes(key + ": " + headers.get(key) + "\r\n");
        }
    }
}
