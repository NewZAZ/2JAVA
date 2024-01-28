package fr.newstaz.istore.model;

import java.util.Objects;

public class User {

    private int id;

    private String email;

    private String password;

    private Role role;

    private boolean isVerified = false;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.role = Role.USER;
    }

    public User(int id, String email, String password, Role role, boolean isVerified) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.isVerified = isVerified;
    }

    public User(String email, String hashpw, Role role) {
        this.email = email;
        this.password = hashpw;
        this.role = role;
    }

    public User(User user) {
        this.id = user.id;
        this.email = user.email;
        this.password = user.password;
        this.role = user.role;
        this.isVerified = user.isVerified;
    }

    public int getId() {
        return id;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return id == user.id && Objects.equals(email, user.email) && Objects.equals(password, user.password) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, role);
    }

    public enum Role {
        ADMIN, USER
    }
}
