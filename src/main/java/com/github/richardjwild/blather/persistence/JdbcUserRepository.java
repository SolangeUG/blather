package com.github.richardjwild.blather.persistence;

import com.github.richardjwild.blather.persistence.dao.UserDAO;
import com.github.richardjwild.blather.user.User;
import com.github.richardjwild.blather.user.UserRepository;

import java.sql.SQLException;
import java.util.Optional;

public class JdbcUserRepository implements UserRepository {

    private UserDAO userDAO;

    public JdbcUserRepository(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public Optional<User> find(String name) {
        User user = null;
        try {
            user = userDAO.findBy(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(user);
    }

    @Override
    public void save(User user) {
        Optional<User> retrievedUser = this.find(user.name());
        if (! retrievedUser.isPresent()) {
            try {
                userDAO.save(user);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                userDAO.updateFollowing(user);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
