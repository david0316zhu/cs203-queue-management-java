package com.ticketmasterdemo.demo.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.Map;


import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.ticketmasterdemo.demo.model.Registration;
import com.ticketmasterdemo.demo.model.User;
import com.ticketmasterdemo.demo.util.Utility;


@Repository
public class EventRegisterRepositoryImpl implements EventRegisterRepository {

    private JdbcTemplate jdbcTemplate;

    public EventRegisterRepositoryImpl(JdbcTemplate template) {
        this.jdbcTemplate = template;
    }

    @Override
    public Registration addRegistration(Registration form) {
        KeyHolder holder = new GeneratedKeyHolder();
        Utility util = new Utility();
        String group_id = util.generateRandomUUID();
        form.setId(group_id);
        try {
            jdbcTemplate.update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                    PreparedStatement statement = conn.prepareStatement("INSERT INTO registration_group_for_event (id, group_size, queue_id, user_id) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                    statement.setString(1, group_id);
                    statement.setInt(2, form.getGroupSize());
                    statement.setString(3, form.getQueueId());
                    statement.setString(4, form.getUser().getId());
                    System.out.println(form.getUser().getId());
                    return statement;
                }
            }, holder);
            
            Map<String,Object> key = holder.getKeys();
            if (key != null) {
                return form;
            } else {
                System.out.println("here");
                throw new RuntimeException("Generated key is null.");
            }
        } catch (DataAccessException e) {
            // Handle database-related exceptions here
            throw new RuntimeException("Error inserting registration: " + e.getMessage(), e);
        }
    }

    public boolean registerUser(User user, String group_id) {
        KeyHolder holder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                    PreparedStatement statement = conn.prepareStatement("INSERT INTO confirmation_group_for_event (user_id, group_id) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
                    statement.setString(1, user.getId()); // Assuming 'id' is of type Long
                    statement.setString(2, group_id); // Assuming 'group_size' is of type int
                    return statement;
                }
            }, holder);
            
            Map<String,Object> key = holder.getKeys();
            if (key != null) {
                return true;
            } else {
                throw new RuntimeException("Generated key is null.");
            }
        } catch (DataAccessException e) {
            // Handle database-related exceptions here
            throw new RuntimeException("Error inserting registration: " + e.getMessage(), e);
        }
    }
}
