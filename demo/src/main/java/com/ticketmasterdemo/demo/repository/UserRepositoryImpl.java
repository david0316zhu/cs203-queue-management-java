package com.ticketmasterdemo.demo.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ticketmasterdemo.demo.model.User;
import com.ticketmasterdemo.demo.repository.UserRepository;
@Repository
public class UserRepositoryImpl implements UserRepository{
    private JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(JdbcTemplate template) {
        this.jdbcTemplate = template;
    }
    
    public Optional<User> findUser(String email) {
        String sql = "SELECT * FROM users WHERE email=?";
        try {
            User user = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                String id = rs.getString("id");
                String userEmail = rs.getString("email");
                boolean authenticated = rs.getBoolean("authenticated");
                return new User(id, userEmail, authenticated);
            }, email);

            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }
    
    
}
