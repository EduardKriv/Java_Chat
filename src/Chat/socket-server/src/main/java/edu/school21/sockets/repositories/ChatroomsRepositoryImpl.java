package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class ChatroomsRepositoryImpl implements ChatroomsRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ChatroomsRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<Chatroom> findById(Long id) {
        String SQL_FIND_BY_ID  = "SELECT * FROM chatrooms WHERE id =?";
        return jdbcTemplate.query(SQL_FIND_BY_ID, new ChatroomRowMapper(), new Object[] { id }).stream().findAny();
    }

    @Override
    public List<Chatroom> findAll() {
        String SQL_FIND_BY_ID  = "SELECT * FROM chatrooms";
        return jdbcTemplate.query(SQL_FIND_BY_ID, new ChatroomRowMapper());
    }

    @Override
    public void save(Chatroom entity) {
        String SQL_INSERT_ANNOTATION = "INSERT INTO chatrooms(chatname, creator) VALUES(?, ?)";

        final KeyHolder id = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT_ANNOTATION, new String[] { "id" } );
            ps.setString(1, entity.getChatName());
            ps.setString(2, entity.getCreator().getName());
            return ps;
        }, id);

        entity.setId(Objects.requireNonNull(id.getKey()).longValue());
    }

    @Override
    public void update(Chatroom entity) {
        jdbcTemplate.update("UPDATE chatrooms SET chatname=?, creator=? WHERE id=?",
                entity.getChatName(), entity.getCreator().getName(), entity.getId());
    }

    @Override
    public void delete(Long entity) {
        jdbcTemplate.update("DELETE FROM chatrooms WHERE id=?", entity);
    }

    class ChatroomRowMapper implements RowMapper<Chatroom> {

        @Override
        public Chatroom mapRow(ResultSet rs, int rowNum) throws SQLException {
            Chatroom chatroom = new Chatroom();
            chatroom.setId(rs.getLong("id"));
            chatroom.setChatName(rs.getString("chatname"));

            String SQL_FIND_BY_ID  = "SELECT * FROM users WHERE name =?";
            String creatorName = rs.getString("creator");

            User creator = jdbcTemplate.query(SQL_FIND_BY_ID, new BeanPropertyRowMapper<>(User.class), new Object[] { creatorName })
                    .stream().findAny().orElse(null);
            chatroom.setCreator(creator);

            return chatroom;
        }
    }
}

