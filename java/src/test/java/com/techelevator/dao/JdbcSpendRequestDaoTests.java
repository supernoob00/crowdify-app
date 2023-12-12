package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class JdbcSpendRequestDaoTests extends BaseDaoTests {
    private JdbcUserDao sut;
    private JdbcSpendRequestDao sut2;
    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcUserDao(jdbcTemplate);
        sut2 = new JdbcSpendRequestDao(jdbcTemplate);
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
    public void getSpendRequestById_returns_a_spend_request_with_valid_id() {
        Assert.assertEquals(REQUEST_1, sut2.getSpendRequestById(REQUEST_1.getId()).orElseThrow());
        Assert.assertEquals(REQUEST_2, sut2.getSpendRequestById(REQUEST_2.getId()).orElseThrow());
    }

    @Test
    public void getSpendRequestsByCampaign_returns_a_spend_request_with_valid_id() {
        List<SpendRequest> returnedSpendReqsList = sut2.getSpendRequestsByCampaign(1);
        Assert.assertEquals(REQUEST_1, returnedSpendReqsList.get(0));

        returnedSpendReqsList = sut2.getSpendRequestsByCampaign(2);
        Assert.assertEquals(REQUEST_2, returnedSpendReqsList.get(0));
        Assert.assertEquals(REQUEST_3, returnedSpendReqsList.get(1));
    }

    @Test
    public void createSpendRequest_creates_new_spend_request() {
        NewSpendRequestDto newSpendRequest = new NewSpendRequestDto(
                1,
                "Test Name",
                1000,
                "Test Description",
                LocalDateTime.of(2024, 1, 25, 0, 0)
        );
        SpendRequest createdRequest = sut2.createSpendRequest(newSpendRequest);
        Optional <SpendRequest> retrievedRequest = sut2.getSpendRequestById(createdRequest.getId());
        Assert.assertTrue(retrievedRequest.isPresent());
        Assert.assertEquals(retrievedRequest.orElseThrow(), createdRequest);
    }

    @Test
    public void updateSpendRequest_updates_a_spend_request() {
        UpdateSpendRequestDto requestToUpdate = new UpdateSpendRequestDto(
                3,
                1000,
                "Mo Money!",
                true,
                LocalDateTime.of(2024, 2, 4, 0, 0)
        );

        SpendRequest updatedRequest = sut2.updateSpendRequest(requestToUpdate, 3);
        Assert.assertNotNull(updatedRequest);

        Optional<SpendRequest> retrievedRequest = sut2.getSpendRequestById(updatedRequest.getId());
        Assert.assertEquals(retrievedRequest.orElseThrow(), updatedRequest);

    }
}
