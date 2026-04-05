package controller;

import db.Repository;
import enumfile.CheckCookie;
import enumfile.HttpHeader;
import enumfile.Login;
import enumfile.QueryKey;
import enumfile.URL;
import model.User;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.util.Map;

import static http.util.HttpRequestUtils.parseQueryParameter;

public class LoginController implements Controller {
    int loginBeforeFlag = Login.UNDEFINED.getFlag();
    Repository repository;

    public LoginController(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(HttpRequest request, HttpResponse response) {
        String userdata = request.getBody();
        loginBeforeFlag = checkUser(userdata);
        if(loginBeforeFlag == Login.LOGIN_FAIL.getFlag()) {
            response.sendRedirect(URL.LOGIN_FAILED_LINK.getLink());
            return;
        }
        response.addHeader(HttpHeader.SET_COOKIE.getHeader(), CheckCookie.LOGIN_TRUE.getLoginCookie());
        response.sendRedirect(URL.INDEX.getLink());
    }

    private int checkUser(String data) {
        Map<String, String> userInstance = parseQueryParameter(data);

        String userId = QueryKey.USERID.safeDecode(userInstance);
        String password = QueryKey.PASSWORD.safeDecode(userInstance);

        User user = repository.findUserById(userId);
        if(user == null) return Login.LOGIN_FAIL.getFlag();
        if(!user.getPassword().equals(password)) return Login.LOGIN_FAIL.getFlag();
        return Login.LOGIN_SUCCESS.getFlag();
    }
}
