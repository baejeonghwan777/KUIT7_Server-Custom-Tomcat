package controller;

import enumfile.Login;
import enumfile.URL;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class ListController implements Controller {
    int loginAfterFlag = Login.UNDEFINED.getFlag();

    @Override
    public void execute(HttpRequest request, HttpResponse response) {
        String url = request.getUrl();
        loginAfterFlag = request.checkCookie();
        if(loginAfterFlag == Login.UNDEFINED.getFlag()) {
            response.sendRedirect(URL.TRY_LOGIN_LINK.getLink());
            return;
        }
        response.sendRedirect(URL.LISTLINK.getLink());
    }
}
