package controller;

import enumfile.URL;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class HomeController implements Controller {
    @Override
    public void execute(HttpRequest request, HttpResponse response) {
        response.sendRedirect(URL.INDEX.getLink());
    }
}
