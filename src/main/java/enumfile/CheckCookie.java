package enumfile;

public enum CheckCookie {
    LOGIN_TRUE("logined=true"),
    LOGIN_FALSE("logined=false");

    private final String cookie;

    CheckCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getLoginCookie() {
        return cookie;
    }
}
