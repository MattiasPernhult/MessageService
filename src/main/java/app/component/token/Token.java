package app.component.token;

import app.component.user.User;

public class Token {
    public static final int EXPIRES_AFTER_SECONDS = 864000; // 10 days

    private User user;
    private String jwtToken;

    public Token() {
    }

    public Token(String userId) {
        this.user = new User(userId, "");
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
