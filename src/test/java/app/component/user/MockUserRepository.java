package app.component.user;

import app.http.exception.ApiException;

public class MockUserRepository implements UserRepository {

    private UserRepository realRepo;
    private boolean failOnCreate;
    private boolean returnNullOnFindUserById;

    public MockUserRepository(UserRepository realRepo) {
        this.realRepo = realRepo;
    }

    MockUserRepository failOnCreate() {
        this.failOnCreate = true;
        return this;
    }

    public MockUserRepository returnNullOnFindUserById() {
        this.returnNullOnFindUserById = true;
        return this;
    }

    @Override
    public void createUser(User user) throws ApiException {
        if (failOnCreate) {
            throw ApiException.ServerError;
        }
        this.realRepo.createUser(user);
    }

    @Override
    public User findUserByName(String name) throws ApiException {
        return this.realRepo.findUserByName(name);
    }

    @Override
    public User findUserById(String id) throws ApiException {
        if (this.returnNullOnFindUserById) {
            return null;
        }

        return this.realRepo.findUserById(id);
    }
}
