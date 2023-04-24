package org.oliverr.bugtracker.repository;

import org.oliverr.bugtracker.DB;
import org.oliverr.bugtracker.entity.Role;
import org.oliverr.bugtracker.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Repository
@Component
public class UserRepository {

    private DB db;

    @Autowired
    public void setDb(DB db) { this.db = db; }

    public User findByEmail(String s) {

        ResultSet rs = db.executeQuery("SELECT * FROM users WHERE email = '"+s+"';");
        User user = null;

        try {
            while(rs.next()) {
                user = new User();
                user.setId(rs.getLong(1));
                user.setFname(rs.getString(2));
                user.setLname(rs.getString(3));
                user.setEmail(rs.getString(4));
                user.setPassword(rs.getString(5));
                user.setCreated(rs.getString(6));
                user.setLastLogin(rs.getString(7));
                user.setImage(rs.getString(8));
            }
            rs.close();
        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }

        if (user != null) {
            ResultSet rs2 = db.executeQuery("SELECT roles.* FROM roles INNER JOIN users_roles ON users_roles.role_id = roles.role_id WHERE users_roles.user_id = "+user.getId()+";");
            try {
                while(rs2.next()) {
                    Role role = new Role();
                    role.setId(rs2.getLong(1));
                    role.setRole(rs2.getString(2));
                    user.addToRoles(role);
                }
                rs2.close();
            } catch(SQLException e) {
                e.printStackTrace();
                return null;
            }
            user.setAdmin(isAdmin(user));
        }

        return user;
    }

    public User findById(Long id) {

        ResultSet rs = db.executeQuery("SELECT * FROM users WHERE user_id = '"+id+"';");
        User user = null;

        try {
            while(rs.next()) {
                user = new User();
                user.setId(rs.getLong(1));
                user.setFname(rs.getString(2));
                user.setLname(rs.getString(3));
                user.setEmail(rs.getString(4));
                user.setPassword(rs.getString(5));
                user.setCreated(rs.getString(6));
                user.setLastLogin(rs.getString(7));
                user.setImage(rs.getString(8));
            }
            rs.close();
        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }

        if (user != null) {
            ResultSet rs2 = db.executeQuery("SELECT roles.* FROM roles INNER JOIN users_roles ON users_roles.role_id = roles.role_id WHERE users_roles.user_id = "+user.getId()+";");
            try {
                while(rs2.next()) {
                    Role role = new Role();
                    role.setId(rs2.getLong(1));
                    role.setRole(rs2.getString(2));
                    user.addToRoles(role);
                }
                rs2.close();
            } catch(SQLException e) {
                e.printStackTrace();
                return null;
            }
            user.setAdmin(isAdmin(user));
        }

        return user;
    }

    public boolean isAdmin(User loggedUser) {
        for(Role r : loggedUser.getRoles()) {
            if(r.getRole().equalsIgnoreCase("admin")) return true;
        }
        return false;
    }

    public void updatePassword(long userId, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPw = encoder.encode(password);

        try {
            PreparedStatement ps = db.conn.prepareStatement("UPDATE users SET password = ? WHERE user_id = ?;");
            ps.setString(1, encodedPw);
            ps.setLong(2, userId);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addUser(String fname, String lname, String email, String password) {
        if(fname.length() >= 100 || lname.length() >= 100 || email.length() >= 100) {
            return;
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPw = encoder.encode(password);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        String current = formatter.format(date);

        try {
            PreparedStatement ps = db.conn.prepareStatement("INSERT INTO users (fname, lname, email, password, image, created, last_login) VALUES (?, ?, ?, ?, '/assets/images/faces/face15.jpg', ?, ?);");
            ps.setString(1, fname);
            ps.setString(2, lname);
            ps.setString(3, email);
            ps.setString(4, encodedPw);
            ps.setString(5, current);
            ps.setString(6, current);

            ps.execute();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void addRole(Long userId, Long roleId) {
        try {
            PreparedStatement ps = db.conn.prepareStatement("INSERT INTO users_roles(user_id, role_id) VALUES (?, ?);");
            ps.setLong(1, userId);
            ps.setLong(2, roleId);

            ps.execute();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<User> getAllUser() {
        ArrayList<User> users = new ArrayList<>();

        ResultSet rs = db.executeQuery("SELECT * FROM users ORDER BY user_id DESC;");

        try {
            while(rs.next()) {
                User user = new User();
                user.setId(rs.getLong(1));
                user.setFname(rs.getString(2));
                user.setLname(rs.getString(3));
                user.setEmail(rs.getString(4));
                user.setPassword(rs.getString(5));
                user.setCreated(rs.getString(6));
                user.setLastLogin(rs.getString(7));
                user.setImage(rs.getString(8));

                users.add(user);
            }
            rs.close();
        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }

        for(User u : users) {
            if (u != null) {
                ResultSet rs2 = db.executeQuery("SELECT roles.* FROM roles INNER JOIN users_roles ON users_roles.role_id = roles.role_id WHERE users_roles.user_id = "+u.getId()+";");
                try {
                    while(rs2.next()) {
                        Role role = new Role();
                        role.setId(rs2.getLong(1));
                        role.setRole(rs2.getString(2));
                        u.addToRoles(role);
                    }
                    rs2.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                    return null;
                }
                u.setAdmin(isAdmin(u));
            }
        }

        return users;
    }

    public void updateLastLoginDate(Long userid) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        String current = formatter.format(date);

        try {
            PreparedStatement ps = db.conn.prepareStatement("UPDATE users SET last_login = ? WHERE user_id = ?;");
            ps.setString(1, current);
            ps.setLong(2, userid);
            ps.execute();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

}
