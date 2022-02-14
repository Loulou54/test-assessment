package co.copper.test.storage;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sbuslab.utils.db.JacksonBeanRowMapper;

import co.copper.test.datamodel.ApiUser;
import co.copper.test.datamodel.User;
import lombok.extern.slf4j.Slf4j;


@Repository
@Slf4j
public class TestJavaRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final JacksonBeanRowMapper<User> rowMapper;

    @Autowired
    public TestJavaRepository(NamedParameterJdbcTemplate jdbcTemplate, ObjectMapper mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new JacksonBeanRowMapper<>(User.class, mapper);
    }

    public List<User> storeUser(ApiUser user) {
        log.debug("Storing user: {}", user);
        final MapSqlParameterSource parametersMap = new MapSqlParameterSource("id", user.getLogin().getUuid())
                .addValue("firstName", user.getName().getFirst())
                .addValue("lastName", user.getName().getLast())
                .addValue("email", user.getEmail())
                .addValue("password", user.getLogin().getPassword()); // TODO: hash the password with sha256 and salt or PBKDF2 or such.
        return jdbcTemplate.query("INSERT INTO Users(id, firstName, lastName, email, password) VALUES (:id, :firstName, :lastName, :email, :password) RETURNING *;",
                parametersMap,
                rowMapper);
    }

}
