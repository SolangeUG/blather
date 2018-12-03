package com.github.richardjwild.blather.persistence;

import com.github.richardjwild.blather.persistence.dao.UserDAO;
import com.github.richardjwild.blather.user.User;
import com.github.richardjwild.blather.user.UserRepository;

import java.sql.SQLException;
import java.util.Optional;

public class JdbcUserRepository implements UserRepository {

    private UserDAO userDAO;

    JdbcUserRepository(UserDAO userDAO) {
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
        try {
            userDAO.save(user);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DuplicateUserNameNotAllowed(e.getMessage());
        }
    }
}
