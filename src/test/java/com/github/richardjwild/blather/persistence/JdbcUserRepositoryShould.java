package com.github.richardjwild.blather.persistence;

import com.github.richardjwild.blather.persistence.dao.UserDAO;
import com.github.richardjwild.blather.user.User;
import com.github.richardjwild.blather.user.UserRepository;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
        given(userDAO.findBy(userName)).willReturn(expectedUser);

        userRepository.save(expectedUser);

        Optional<User> actualUser = userRepository.find(userName);

        verify(userDAO).save(expectedUser);
        assertThat(actualUser.isPresent()).isTrue();
        assertThat(actualUser.get()).isSameAs(expectedUser);
    }

    @Test(expected = DuplicateUserNameNotAllowed.class)
    public void not_store_duplicate_users_when_the_same_user_saved_twice() {
        String userName = "user_name";
        User user = new User(userName);

        userRepository = new JdbcUserRepository(new UserDAO(getConnection()));

        userRepository.save(user);
        User userWithSameName = new User(userName);

        userRepository.save(userWithSameName);
    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/test_blather",
                    "postgres",
                    "postgres"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
