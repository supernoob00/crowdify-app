package com.techelevator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.lang.Nullable;

import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class User {
   @Min(1)
   private int id;
   @NotBlank
   private String username;
   @Email @Nullable
   private String email;
   @JsonIgnore @NotBlank @Min(8)
   private String password;
   @JsonIgnore
   private boolean activated;
   @NotNull @NotEmpty
   private Set<Authority> authorities = new HashSet<>();

   public User() {
   }

   public User(int id, String username, String password, String authorities) {
      this.id = id;
      this.username = username;
      this.password = password;
      if (authorities != null) this.setAuthoritiesFromString(authorities);
      this.activated = true;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public boolean isActivated() {
      return activated;
   }

   public void setActivated(boolean activated) {
      this.activated = activated;
   }

   public Set<Authority> getAuthorities() {
      return authorities;
   }

   public void setAuthoritiesFromString(Set<Authority> authorities) {
      this.authorities = authorities;
   }

   public void setAuthoritiesFromString(String authorities) {
      String[] roles = authorities.split(",");
      for (String role : roles) {
         String authority = role.contains("ROLE_") ? role : "ROLE_" + role;
         this.authorities.add(new Authority(authority));
      }
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      User user = (User) o;
      return id == user.id && activated == user.activated && username.equals(user.username) && Objects.equals(email, user.email) && password.equals(user.password);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, username, email, password, activated);
   }

   @Override
   public String toString() {
      return "User{" +
              "id=" + id +
              ", username='" + username + '\'' +
              ", email='" + email + '\'' +
              ", password='" + password + '\'' +
              ", activated=" + activated +
              ", authorities=" + authorities +
              '}';
   }
}
