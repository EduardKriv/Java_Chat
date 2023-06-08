package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class UsersRepositoryImpl implements UsersRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UsersRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<User> findByName(String name) {
        return jdbcTemplate.query("SELECT * FROM users WHERE name =?", new BeanPropertyRowMapper<>(User.class), new Object[] { name }).stream().findAny();
    }

    @Override
    public Optional<User> findById(Long id) {
        String SQL_FIND_BY_ID  = "SELECT * FROM users WHERE id =?";
        return jdbcTemplate.query(SQL_FIND_BY_ID, new BeanPropertyRowMapper<>(User.class), new Object[] { id }).stream().findAny();
    }

    @Override
    public List<User> findAll() {
        String SQL_FIND_BY_ID  = "SELECT * FROM users";
        return jdbcTemplate.query(SQL_FIND_BY_ID, new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public void save(User entity) {
        jdbcTemplate.update("INSERT INTO users(name, password) VALUES(?, ?)", entity.getName(), entity.getPassword());
    }

    @Override
    public void update(User entity) {
        jdbcTemplate.update("UPDATE users SET name=? WHERE id=?", entity.getName(), entity.getId());
    }

    @Override
    public void delete(Long entity) {
        jdbcTemplate.update("DELETE FROM users WHERE id=?", entity);
    }

    public void saveMessage(Message message, User user) {
        jdbcTemplate.update("INSERT INTO messages(sender, text, time) VALUES(?, ?, ?)",
                user.getName(), message.getText(), Timestamp.valueOf(LocalDateTime.now()));
    }
}
