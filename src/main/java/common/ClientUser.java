package common;

public class ClientUser
{
    private AuthUser authUser;
    private String accessToken;
    private boolean success;

    public AuthUser getUser() {
        return authUser;
    }

    public void setUser(AuthUser userAuthData) {
        this.authUser = userAuthData;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
