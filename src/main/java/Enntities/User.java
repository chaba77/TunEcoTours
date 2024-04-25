package Enntities;

import java.util.List;

public class User {
    private int id;
    private String email;
    private String password;
    private List<String> roles;
    private int is_verified;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public int getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(int is_verified) {
        this.is_verified = is_verified;
    }

    public User(String email, String password, List<String> roles, int is_verified) {
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.is_verified = is_verified;
    }
    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles='" + roles + '\'' +
                ", is_verified=" + is_verified +
                '}';
    }

    public User(int id, String email, String password, List<String> roles, int is_verified) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.is_verified = is_verified;
    }

}
