package services;

import utils.MyDatabase;
import Enntities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.google.gson.Gson;


import at.favre.lib.crypto.bcrypt.BCrypt;

import at.favre.lib.crypto.bcrypt.BCrypt.Hasher;
import at.favre.lib.crypto.bcrypt.BCrypt.Version;



public class userservice {
    private Connection connection;

    public userservice() {
        connection = MyDatabase.getInstance().getConnection();
    }

    public boolean authenticate(String email, String password) throws SQLException {
        String query = "SELECT password FROM user WHERE email = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, email);
        ResultSet resultSet = ps.executeQuery();

        if (resultSet.next()) {
            String hashedPasswordFromDB = resultSet.getString("password");
            // Use BCrypt.verifyer() to verify the password
            boolean isPasswordCorrect = BCrypt.verifyer().verify(password.toCharArray(), hashedPasswordFromDB).verified;
            return isPasswordCorrect;
        } else {
            return false; // No user found with the provided email
        }
    }
    public User getUserByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM user WHERE email = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            String rolesString = rs.getString("roles");
            List<String> rolesList = Arrays.asList(rolesString.split(","));
            user.setRoles(rolesList);
            user.setPassword(rs.getString("password"));
            return user;
        } else {
            return null; // No user found with the provided email
        }
    }
    public boolean emailExists(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM user WHERE email = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, email);
        ResultSet resultSet = ps.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1) > 0;
        } else {
            return false; // No rows returned
        }
    }


    public void create(User user) throws SQLException {
        Hasher argon2Hasher = BCrypt.with(Version.VERSION_2A); // Use Argon2 with bcrypt compatibility
        String hashedPassword = argon2Hasher.hashToString(12, user.getPassword().toCharArray());

        String rolesJson = new Gson().toJson(user.getRoles()); // Convert roles list to JSON string
        String sql = "INSERT INTO user (email, password, roles) VALUES ('"
                + user.getEmail() + "', '"
                + hashedPassword + "', '"
                + rolesJson + "')";

        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }
    public void deleteUser(String email) throws SQLException {
        // Assuming you have a connection setup to your database
        String sql = "DELETE FROM user WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.executeUpdate();
        }
    }
    public void updateUserEmail(String currentEmail, String newEmail) throws SQLException {
        String sql = "UPDATE user SET email = ? WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newEmail);
            stmt.setString(2, currentEmail);
            stmt.executeUpdate();
        }
    }
    public void updateUserPassword(String currentEmail, String plainNewPassword) throws SQLException {
        Hasher argon2Hasher = BCrypt.with(Version.VERSION_2A); // Use Argon2 with bcrypt compatibility
        String hashedPassword = argon2Hasher.hashToString(12, plainNewPassword.toCharArray());

        // SQL to update the password
        String sql = "UPDATE user SET password = ? WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hashedPassword);
            stmt.setString(2, currentEmail);
            int affectedRows = stmt.executeUpdate();

            // Check if the update was successful
            if (affectedRows == 0) {
                throw new SQLException("Updating password failed, no rows affected.");
            }
        }
    }
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, email, password, roles, is_verified FROM user";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                users.add(new User(rs.getInt("id"), rs.getString("email"), rs.getString("password"), List.of(rs.getString("roles").split(",")), rs.getInt("is_verified")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void deleteUser(int id) {
        String sql = "DELETE FROM user WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateUser(User user) {
        String sql = "UPDATE user SET email = ?, password = ?, roles = ?, is_verified = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, String.join(",", user.getRoles()));
            pstmt.setInt(4, user.getIs_verified());
            pstmt.setInt(5, user.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
