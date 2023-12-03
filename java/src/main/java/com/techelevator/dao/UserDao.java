package com.techelevator.dao;

import com.techelevator.model.RegisterUserDto;
import com.techelevator.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    List<User> getUsers();

    Optional<User> getUserById(int id);

    Optional<User> getUserByUsername(String username);

    User createUser(RegisterUserDto user);
}
