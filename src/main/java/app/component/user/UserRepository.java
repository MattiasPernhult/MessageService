package app.component.user;

import app.http.exception.ApiException;

public interface UserRepository {
    void createUser(User user) throws ApiException;

    User findUserByName(String name) throws ApiException;

    User findUserById(String id) throws ApiException;
}
