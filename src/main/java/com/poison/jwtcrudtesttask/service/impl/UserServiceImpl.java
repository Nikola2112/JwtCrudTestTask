package com.poison.jwtcrudtesttask.service.impl;

import com.poison.jwtcrudtesttask.models.User;
import com.poison.jwtcrudtesttask.repository.UserRepository;
import com.poison.jwtcrudtesttask.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<User> updateUser(Long id, User updatedUser) {
        return repository.findById(id).map(existingUser -> {
            existingUser.setName(updatedUser.getName());
            existingUser.setAge(updatedUser.getAge());
            if (!updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
            return repository.save(existingUser);
        });
    }

    @Override
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    @Override
    public User getUserByName(String name) {
        return repository.findAll().stream()
                .filter(user -> user.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}