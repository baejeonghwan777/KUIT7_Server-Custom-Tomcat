package enumfile;

public enum URL {
    SIGN_UP("/user/signup"),
    LOGIN("/user/login"),
    LOGIN_FAILED_LINK("/user/login_failed.html"),
    TRY_LOGIN_LINK("/user/login.html"),
    CSS("text/css"),
    HTML("text/html;charset=utf-8"),
    LIST("/user/userList"),
    WEBAPP("/webapp"),
    INDEX("/index.html");

    private final String link;

    URL(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }
}
