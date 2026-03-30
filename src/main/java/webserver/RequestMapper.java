package webserver;

import controller.Controller;
import controller.ForwardController;
import controller.HomeController;
import controller.ListController;
import controller.LoginController;
import controller.SignUpController;

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

        if (httpRequest.getUrl().equals("/user/signup")) {
            controller = new SignUpController();
        }

        if (httpRequest.getUrl().equals("/user/login")) {
            controller = new LoginController();
        }

        if (httpRequest.getUrl().equals("/user/userList")) {
            controller = new ListController();
        }
        controller.execute(httpRequest, httpResponse);
    }
}
