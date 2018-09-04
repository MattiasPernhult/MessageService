package app.component.user;

import app.db.MemoryDB;
import app.db.MemoryDBTestUtil;
import app.http.exception.ApiException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    @Test
    void testCreate_OK() {
        try {
            MemoryDB memoryDB = new MemoryDB();

            User user = new User(UUID.randomUUID().toString(), "test user");
            UserService userService = new UserServiceMemoryDBFactory(
                    memoryDB
            ).create();

            userService.create(user);

            User storedUser = memoryDB.findUserById(user.getId());
            assertEquals(user, storedUser);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testCreate_UserAlreadyExists() {
        try {
            MemoryDB memoryDB = new MemoryDB();
            User user = MemoryDBTestUtil.createUser(memoryDB);

            UserService userService = new UserServiceMemoryDBFactory(
                    memoryDB
            ).create();

            User newUser = new User(UUID.randomUUID().toString(), user.getName());
            ApiException e = assertThrows(ApiException.class, () -> {
                userService.create(newUser);
            });
            assertEquals(ApiException.UserAlreadyExists.getMessage(), e.getMessage());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testCreate_FailsOnCreate() {
        try {
            MemoryDB memoryDB = new MemoryDB();
            UserService userService = new UserServiceImpl(
                    new MockUserRepository(memoryDB).failOnCreate()
            );

            User newUser = new User(UUID.randomUUID().toString(), "test user");
            ApiException e = assertThrows(ApiException.class, () -> {
                userService.create(newUser);
            });
            assertEquals(ApiException.ServerError.getMessage(), e.getMessage());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
