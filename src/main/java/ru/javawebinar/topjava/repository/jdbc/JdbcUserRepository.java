package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private static final RowMapper<User> CUSTOM_ROW_MAPPER = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getInt("ID"));
        user.setName(rs.getString("NAME"));
        user.setEmail(rs.getString("EMAIL"));
        user.setPassword(rs.getString("PASSWORD"));
        user.setRegistered(rs.getObject("REGISTERED", Date.class));
        user.setEnabled(rs.getBoolean("ENABLED"));
        user.setCaloriesPerDay(rs.getInt("CALORIES_PER_DAY"));

        String role = rs.getString("ROLE");
        if (Objects.isNull(role)) user.setRoles(Collections.emptySet());
        else user.setRoles(Set.of(Role.valueOf(role)));
        return user;
    };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            StringBuilder sb = new StringBuilder();
            for (Role r : user.getRoles()){
                sb.append(r.toString());
            }

            jdbcTemplate.update("insert into user_roles (user_id, role) VALUES (?, ?)", user.getId(), user.getRoles().toArray(new Role[0])[0].toString());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        }
//
//        jdbcTemplate.batchUpdate(
//                "update users set left outer join  roles = ? where id = ?",
//                new BatchPreparedStatementSetter() {
//
//                    public void setValues(PreparedStatement ps, int i)
//                            throws SQLException {
//                        ps.setBigDecimal(1, books.get(i).getPrice());
//                        ps.setLong(2, books.get(i).getId());
//                    }
//
//                    public int getBatchSize() {
//                        return books.size();
//                    }
//
//                });

        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        System.out.println("JDBC");
        List<User> users = jdbcTemplate.query("SELECT * FROM users LEFT OUTER JOIN USER_ROLES on USERS.ID = USER_ROLES.USER_ID WHERE id=?", CUSTOM_ROW_MAPPER, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users LEFT OUTER JOIN USER_ROLES on USERS.ID = USER_ROLES.USER_ID WHERE email=?", CUSTOM_ROW_MAPPER, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users LEFT OUTER JOIN USER_ROLES on USERS.ID = USER_ROLES.USER_ID ORDER BY name, email", CUSTOM_ROW_MAPPER);
    }
}
