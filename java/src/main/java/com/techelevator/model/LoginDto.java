package com.techelevator.model;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

/*
    The acronym DTO is being used for "data transfer object". It means that this type of class is specifically
    created to transfer data between the client and the server. For example, LoginDto represents the data a
    client must pass to the server for a login endpoint, and LoginResponseDto represents the object that's returned
    from the server to the client from a login endpoint.
 */
public class LoginDto {
   @NotBlank
   private String username;
   @NotBlank
   private String password;

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

   @Override
   public String toString() {
      return "LoginDTO{" +
              "username='" + username + '\'' +
              ", password='" + password + '\'' +
              '}';
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      LoginDto loginDto = (LoginDto) o;
      return username.equals(loginDto.username) && password.equals(loginDto.password);
   }

   @Override
   public int hashCode() {
      return Objects.hash(username, password);
   }
}
