package controller;

import db.MemoryUserRepository;
import db.Repository;
import enumfile.QueryKey;
import enumfile.URL;
import model.User;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.util.Map;

import static http.util.HttpRequestUtils.parseQueryParameter;

public class SignUpController implements Controller {
    Repository repository;

    public SignUpController(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(HttpRequest request, HttpResponse response) {
        String userdata = request.getBody();
        makeUser(userdata);
        response.sendRedirect(URL.INDEX.getLink());
    }

    private void makeUser(String data) {
        Map<String, String> userInstance = parseQueryParameter(data);

        String userId = QueryKey.USERID.safeDecode(userInstance);
        String password = QueryKey.PASSWORD.safeDecode(userInstance);
        String name = QueryKey.NAME.safeDecode(userInstance);
        String email = QueryKey.EMAIL.safeDecode(userInstance);

        User user = repository.findUserById(userId);
        if(user == null) {
            user = new User(userId, password, name, email);
            repository.addUser(user);
        }
    }
}
