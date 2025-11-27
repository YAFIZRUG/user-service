package edu.aston.dao;

import edu.aston.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserDaoImplTest extends BaseDaoTest {

    private UserDao userDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(testSessionFactory);
    }

    @Test
    void create_shouldSaveUserToDatabase() {

        User user = new User("Test User", 25, "test@example.com");

        User savedUser = userDao.create(user);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("Test User");
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(savedUser.getAge()).isEqualTo(25);
    }

    @Test
    void findById_shouldReturnUserWhenExists() {

        User user = new User("Test User", 25, "test@example.com");
        User savedUser = userDao.create(user);

        User foundUser = userDao.findById(savedUser.getId());

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(savedUser.getId());
        assertThat(foundUser.getName()).isEqualTo("Test User");
        assertThat(foundUser.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void findById_shouldReturnNullWhenUserNotExists() {

        User foundUser = userDao.findById(999L);

        assertThat(foundUser).isNull();
    }

    @Test
    void findAll_shouldReturnAllUsers() {

        userDao.create(new User("User1", 25, "user1@example.com"));
        userDao.create(new User("User2", 30, "user2@example.com"));

        List<User> users = userDao.findAll();

        assertThat(users).hasSize(2);
    }

    @Test
    void findAll_shouldReturnEmptyListWhenNoUsers() {

        List<User> users = userDao.findAll();
        assertThat(users).isEmpty();
    }

    @Test
    void update_shouldModifyExistingUser() {

        User user = new User("Old Name", 25, "old@example.com");
        User savedUser = userDao.create(user);

        savedUser.setName("New Name");
        savedUser.setEmail("new@example.com");
        savedUser.setAge(30);
        userDao.update(savedUser);

        User updatedUser = userDao.findById(savedUser.getId());
        assertThat(updatedUser.getName()).isEqualTo("New Name");
        assertThat(updatedUser.getEmail()).isEqualTo("new@example.com");
        assertThat(updatedUser.getAge()).isEqualTo(30);
    }

    @Test
    void delete_shouldRemoveUserFromDatabase() {

        User user = new User("To Delete", 25, "delete@example.com");
        User savedUser = userDao.create(user);

        userDao.delete(savedUser.getId());

        User deletedUser = userDao.findById(savedUser.getId());
        assertThat(deletedUser).isNull();
    }
}