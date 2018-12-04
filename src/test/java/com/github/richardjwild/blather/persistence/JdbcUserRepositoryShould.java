package com.github.richardjwild.blather.persistence;

import com.github.richardjwild.blather.helper.DBHelper;
import com.github.richardjwild.blather.persistence.dao.UserDAO;
import com.github.richardjwild.blather.user.User;
import com.github.richardjwild.blather.user.UserRepository;
import org.junit.AfterClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Optional;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class JdbcUserRepositoryShould {

    private UserDAO userDAO = mock(UserDAO.class);
    private UserRepository userRepository = new JdbcUserRepository(userDAO);

    @Test
    public void return_empty_when_user_not_found() throws SQLException {
        String will_not_be_found = "will_not_be_found";
        given(userDAO.findBy(will_not_be_found)).willThrow(SQLException.class);

        Optional<User> result = userRepository.find(will_not_be_found);

        assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void return_stored_user_when_user_is_found() throws SQLException {
        String userName = "will_be_found";
        User expectedUser = new User(userName);

        userDAO = new UserDAO(DBHelper.getConnection());
        userRepository = new JdbcUserRepository(userDAO);

        userRepository.save(expectedUser);

        Optional<User> actualUser = userRepository.find(userName);
        User retrievedUser = userDAO.findBy(userName);

        assertThat(actualUser.isPresent()).isTrue();
        assertEquals(actualUser.get(), retrievedUser);
    }

    @Test
    public void not_store_duplicate_users_when_the_same_user_saved_twice() {
        String userName = "Dinah";
        User user = new User(userName);

        userRepository = new JdbcUserRepository(new UserDAO(DBHelper.getConnection()));
        userRepository.save(user);

        User userWithSameName = new User(userName);
        userRepository.save(userWithSameName);

        Optional<User> retrievedUser = userRepository.find(userName);

        assertEquals(retrievedUser.get(), user);
        assertEquals(retrievedUser.get(), userWithSameName);
    }

//    @Test
//    public void store_users_following() {
//        User user = new User("Jolene");
//        user.follow(new User("rich"));
//        user.follow(new User("sarah"));
//
//        userRepository = new JdbcUserRepository(new UserDAO(DBHelper.getConnection()));
//        userRepository.save(user);
//
//        Optional<User> retrievedUser = userRepository.find("Jolene");
//
//    }

    @AfterClass
    public static void tearDown() {
        DBHelper.clearTestData();
        DBHelper.clearConnection();
    }
}
