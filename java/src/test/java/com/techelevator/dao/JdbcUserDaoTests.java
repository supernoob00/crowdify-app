package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.RegisterUserDto;
import com.techelevator.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

public class JdbcUserDaoTests extends BaseDaoTests {
    private JdbcUserDao sut;
    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcUserDao(jdbcTemplate);
    }

    @Test
    public void getUserByUsername_given_invalid_username_returns_null() {
        Assert.assertTrue(sut.getUserByUsername("invalid").isEmpty());
    }

    @Test
    public void getUserByUsername_given_valid_user_returns_user() {
        User actualUser = sut.getUserByUsername(USER_1.getUsername()).orElseThrow();
        Assert.assertEquals(USER_1, actualUser);
    }

    @Test
    public void getUserById_given_invalid_user_id_returns_null() {
        Optional<User> actualUser = sut.getUserById(-1);
        Assert.assertTrue(actualUser.isEmpty());
    }

    @Test
    public void getUserById_given_valid_user_id_returns_user() {
        User actualUser = sut.getUserById(USER_1.getId()).orElseThrow();
        Assert.assertEquals(USER_1, actualUser);
    }

    @Test
    public void getUsers_returns_all_users() {
        List<User> users = sut.getUsers();

        Assert.assertNotNull(users);
        Assert.assertEquals(8, users.size());
        Assert.assertEquals(USER_1, users.get(0));
        Assert.assertEquals(USER_2, users.get(1));
        Assert.assertEquals(USER_3, users.get(2));
    }

    @Test(expected = DaoException.class)
    public void createUser_with_null_username() {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setUsername(null);
        registerUserDto.setPassword(USER_3.getPassword());
        registerUserDto.setRole("ROLE_USER");
        sut.createUser(registerUserDto);
    }

    @Test(expected = DaoException.class)
    public void createUser_with_existing_username() {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setUsername(USER_1.getUsername());
        registerUserDto.setPassword(USER_3.getPassword());
        registerUserDto.setRole("ROLE_USER");
        sut.createUser(registerUserDto);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createUser_with_null_password() {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setUsername(USER_3.getUsername());
        registerUserDto.setPassword(null);
        registerUserDto.setRole("ROLE_USER");
        sut.createUser(registerUserDto);
    }

    @Test
    public void createUser_creates_a_user() {
        RegisterUserDto user = new RegisterUserDto();
        user.setUsername("new");
        user.setPassword("user");
        user.setRole("ROLE_USER");
        User createdUser = sut.createUser(user);

        Assert.assertNotNull(createdUser);

        User retrievedUser =
                sut.getUserByUsername(createdUser.getUsername()).orElseThrow();
        Assert.assertEquals(retrievedUser, createdUser);
    }

    @Test
    public void get_creator_by_campaign_id_returns_correct_creator() {
        User creator = sut.getCreatorByCampaignId(1).orElseThrow();
        Assert.assertEquals(USER_1, creator);

        Optional<User> invalid = sut.getCreatorByCampaignId(99);
        Assert.assertTrue(invalid.isEmpty());
    }
}
