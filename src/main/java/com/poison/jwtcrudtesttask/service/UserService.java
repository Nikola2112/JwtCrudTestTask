package com.poison.jwtcrudtesttask.service;

import com.poison.jwtcrudtesttask.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    Optional<User> updateUser(Long id, User updatedUser);

    void deleteUser(Long id);

    User getUserByName(String name);
}
