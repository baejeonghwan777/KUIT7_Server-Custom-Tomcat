package webserver;

import db.MemoryUserRepository;
import http.util.HttpRequestUtils;
import http.util.IOUtils;
import model.User;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static http.util.HttpRequestUtils.parseQueryParameter;

public class RequestHandler implements Runnable{
    private static final int UNDEFINED = 0;
    private static final int LOGIN_SUCCESS = 1;
    private static final int LOGIN_FAIL = 2;

    Socket connection;
    private static final Logger log = Logger.getLogger(RequestHandler.class.getName());

    public RequestHandler(Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        log.log(Level.INFO, "New Client Connect! Connected IP : " + connection.getInetAddress() + ", Port : " + connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()){
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line = br.readLine();
            if (line == null) return;
            String firstLine = line;
            log.log(Level.INFO,"request line : { " + line + " }");

            String url = HttpRequestUtils.extractPath(line); // url을 특정 조건에서만 빼옴 반복문 안에서 하나씩 하다가
            int loginBeforeFlag = UNDEFINED;
            int loginAfterFlag = UNDEFINED;
            int length = 0;

            // 클라이언트 데이터를 line by line으로 읽음 로그를 사용하면 쓰레드 출처도 확인 가능
            while(true) {
                line = br.readLine();
                if (line == null || line.isEmpty()) {
                    break;
                }
                if(line.startsWith("Content-Length")) length = HttpRequestUtils.extractLength(line);
                if(line.startsWith("Cookie: logined=true")) loginAfterFlag = LOGIN_SUCCESS;
                log.log(Level.INFO,"request line : { " + line + " }");
            }

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body;

            if (firstLine.startsWith("POST")) {
                String bodyData = IOUtils.readData(br, length);
                log.log(Level.INFO,"POST body : { " + bodyData + " }");
                if(url.startsWith("/user/signup")) makeUser(bodyData);
                if(url.startsWith("/user/login")) loginBeforeFlag = checkUser(bodyData);
                if(loginBeforeFlag == LOGIN_FAIL) {
                    response302Header(dos, "/user/login_failed.html", 0, loginBeforeFlag); // 302 리다이렉트 시 body 불필요
                    return;
                }
                response302Header(dos, "/index.html", 0, loginBeforeFlag);
                return;
            }
            if (firstLine.startsWith("GET")) {
                body = HttpRequestUtils.readPath("./webapp", url);
                if(url.startsWith("/user/userList") && loginAfterFlag == UNDEFINED) {
                    response302Header(dos, "/user/login.html", 0, loginBeforeFlag);
                    return;
                }
                if(url.endsWith(".css")) response200Header(dos, body.length, "text/css");
                if(!url.endsWith(".css")) response200Header(dos, body.length, "text/html;charset=utf-8");
                responseBody(dos, body);
            }

        } catch (IOException e) {
            log.log(Level.SEVERE,e.getMessage());
        }
    }

    private int checkUser(String data) {
        Map<String, String> userInstance = parseQueryParameter(data);

        String userId = URLDecoder.decode(userInstance.get("userId"), StandardCharsets.UTF_8);
        String password = URLDecoder.decode(userInstance.get("password"), StandardCharsets.UTF_8);

        User user = MemoryUserRepository.getInstance().findUserById(userId);
        if(user == null) return LOGIN_FAIL;
        if(!user.getPassword().equals(password)) return LOGIN_FAIL;
        return LOGIN_SUCCESS;
    }

    private void makeUser(String data) {
        Map<String, String> userInstance = parseQueryParameter(data);

        String userId = URLDecoder.decode(userInstance.get("userId"), StandardCharsets.UTF_8);
        String password = URLDecoder.decode(userInstance.get("password"), StandardCharsets.UTF_8);
        String name = URLDecoder.decode(userInstance.get("name"), StandardCharsets.UTF_8);
        String email = URLDecoder.decode(userInstance.get("email"), StandardCharsets.UTF_8);

        User user = MemoryUserRepository.getInstance().findUserById(userId);
        if(user == null) {
            user = new User(userId, password, name, email);
            MemoryUserRepository.getInstance().addUser(user);
        }
    }

    private void response302Header(DataOutputStream dos, String redirectPath, int lengthOfBodyContent, int flag) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + redirectPath + "\r\n");
            if(flag == LOGIN_SUCCESS) dos.writeBytes("Set-Cookie: logined=true\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.log(Level.SEVERE,e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + "\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

}