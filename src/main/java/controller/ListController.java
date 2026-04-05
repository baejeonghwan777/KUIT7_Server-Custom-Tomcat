package controller;

import enumfile.CheckCookie;
import enumfile.Login;
import enumfile.URL;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class ListController implements Controller {
    @Override
    public void execute(HttpRequest request, HttpResponse response) {
        int loginAfterFlag;

        loginAfterFlag = checkCookie(request);
        if(loginAfterFlag == Login.UNDEFINED.getFlag()) {
            response.sendRedirect(URL.TRY_LOGIN_LINK.getLink());
            return;
        }
        response.sendRedirect(URL.LISTLINK.getLink());
    }

    public int checkCookie(HttpRequest request) {
        if(request.getCookie().contains(CheckCookie.LOGIN_TRUE.getLoginCookie())) return Login.LOGIN_SUCCESS.getFlag();
        return Login.UNDEFINED.getFlag();
    }
}
