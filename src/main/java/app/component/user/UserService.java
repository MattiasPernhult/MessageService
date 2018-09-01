package app.component.user;

import app.http.exception.ApiException;

public interface UserService {
    void create(User user) throws ApiException;
}
