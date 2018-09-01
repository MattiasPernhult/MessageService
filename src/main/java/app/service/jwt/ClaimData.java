package app.service.jwt;

public class ClaimData {
    private String userId;

    public ClaimData(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
