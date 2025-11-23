package edu.aston.dao;

import java.util.List;

import edu.aston.model.User;

public interface UserDao {
    
    User create(User user);

    User findById(Long id);

    List<User> findAll();

    void update(User user);

    void delete(Long id);
}
