package app.component.user;

import app.http.exception.ApiException;

class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void create(User user) throws ApiException {
        User storedUser = this.userRepository.findUserByName(user.getName());
        if (storedUser != null) {
            throw ApiException.UserAlreadyExists.setDetail(
                    String.format("with name %s", user.getName())
            );
        }

        this.userRepository.createUser(user);
    }
}
