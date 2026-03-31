package webserver;

import controller.Controller;
import controller.ForwardController;
import controller.HomeController;
import controller.ListController;
import controller.LoginController;
import controller.SignUpController;
import enumfile.URL;

public class RequestMapper {
    private final HttpRequest httpRequest;
    private final HttpResponse httpResponse;
    private Controller controller = new ForwardController();

    public RequestMapper(HttpRequest httpRequest, HttpResponse httpResponse) {
        this.httpRequest = httpRequest;
        this.httpResponse = httpResponse;
    }

    public void proceed() {
        if (httpRequest.getMethod().equals("GET") && httpRequest.getUrl().endsWith(".html")) {
            controller = new ForwardController();
        }

        if (httpRequest.getUrl().equals("/")) {
            controller = new HomeController();
        }

        if (httpRequest.getUrl().equals(URL.SIGN_UP.getLink())) {
            controller = new SignUpController();
        }

        if (httpRequest.getUrl().equals(URL.LOGIN.getLink())) {
            controller = new LoginController();
        }

        if (httpRequest.getUrl().equals(URL.LIST.getLink())) {
            controller = new ListController();
        }
        controller.execute(httpRequest, httpResponse);
    }
}
