package com.techelevator.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/*
    The acronym DTO is being used for "data transfer object". It means that this type of class is specifically
    created to transfer data between the client and the server. For example, CredentialsDto represents the data a client must
    pass to the server for a login endpoint, and TokenDto represents the object that's returned from the server
    to the client from a login endpoint.
 */
public class RegisterUserDto {
    @NotNull @NotBlank
    private String username;
    @NotNull @NotBlank
    private String password;
    @NotNull @NotBlank
    private String confirmPassword;
    @NotNull @NotBlank(message = "Please select a role for this user.")
    private String role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisterUserDto that = (RegisterUserDto) o;
        return username.equals(that.username) && password.equals(that.password) && confirmPassword.equals(that.confirmPassword) && role.equals(that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, confirmPassword, role);
    }
}
