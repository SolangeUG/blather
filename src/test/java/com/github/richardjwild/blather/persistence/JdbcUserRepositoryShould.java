package com.github.richardjwild.blather.persistence;

import com.github.richardjwild.blather.helper.DataBaseHelper;
import com.github.richardjwild.blather.persistence.dao.UserDAO;
import com.github.richardjwild.blather.user.User;
import com.github.richardjwild.blather.user.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class JdbcUserRepositoryShould {

    private UserDAO userDAO = mock(UserDAO.class);
    private UserRepository userRepository = new JdbcUserRepository(userDAO);
    private Connection connection;

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

        userDAO = new UserDAO(connection);
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

        userRepository = new JdbcUserRepository(new UserDAO(connection));
        userRepository.save(user);

        User userWithSameName = new User(userName);
        userRepository.save(userWithSameName);

        Optional<User> retrievedUser = userRepository.find(userName);

        assertEquals(retrievedUser.get(), user);
        assertEquals(retrievedUser.get(), userWithSameName);
    }

    @Test
    public void store_users_following() {
        User jolene = new User("Jolene");
        User rich = new User("Rich");
        User sarah = new User("Sarah");
        User teddy = new User("Teddy");
        User bear = new User("Bear");

        jolene.follow(rich);
        jolene.follow(sarah);
        jolene.follow(teddy);

        userRepository = new JdbcUserRepository(new UserDAO(connection));
        userRepository.save(rich);
        userRepository.save(sarah);
        userRepository.save(teddy);
        userRepository.save(bear);

        userRepository.save(jolene);

        jolene.follow(bear);
        userRepository.save(jolene);

        Optional<User> retrievedUser = userRepository.find("Jolene");

        List<User> usersFollowing = new ArrayList<>(retrievedUser.get().getUsersFollowing());

        assertTrue(usersFollowing.contains(rich));
        assertTrue(usersFollowing.contains(sarah));
        assertTrue(usersFollowing.contains(teddy));
        assertTrue(usersFollowing.contains(bear));
    }

    @Test
    public void a_user_should_not_follow_themselves() {
        User rich = new User("Rich");
        userRepository = new JdbcUserRepository(new UserDAO(connection));
        userRepository.save(rich);

        Optional<User> retrievedUser = userRepository.find("Rich");

        List<User> usersFollowing = new ArrayList<>(retrievedUser.get().getUsersFollowing());

        assertTrue(usersFollowing.isEmpty());

    }

    @Before
    public void setUp() {
        connection = DataBaseHelper.getConnection();
    }

    @After
    public void tearDown() {
        DataBaseHelper.clearTestData(connection);
        DataBaseHelper.clearConnection(connection);
    }
}
