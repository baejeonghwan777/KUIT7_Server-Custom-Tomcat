package webserver;

import controller.Controller;
import controller.ForwardController;
import controller.HomeController;
import controller.ListController;
import controller.LoginController;
import controller.SignUpController;
import db.MemoryUserRepository;
import db.Repository;
import enumfile.URL;

public class RequestMapper {
    private final HttpRequest httpRequest;
    private final HttpResponse httpResponse;
    private Controller controller = new ForwardController();
    Repository repository = MemoryUserRepository.getInstance();

    public RequestMapper(HttpRequest httpRequest, HttpResponse httpResponse) {
        this.httpRequest = httpRequest;
        this.httpResponse = httpResponse;
    }

    public void proceed() {
        if (httpRequest.getMethod().equals("GET") && httpRequest.getPath().endsWith(".html")) {
            controller = new ForwardController();
        }

        if (httpRequest.getPath().equals("/")) {
            controller = new HomeController();
        }

        if (httpRequest.getPath().equals(URL.SIGN_UP.getLink())) {
            controller = new SignUpController(repository);
        }

        if (httpRequest.getPath().equals(URL.LOGIN.getLink())) {
            controller = new LoginController(repository);
        }

        if (httpRequest.getPath().equals(URL.LIST.getLink())) {
            controller = new ListController();
        }
        controller.execute(httpRequest, httpResponse);
    }
}
