package co.copper.test.storage

import java.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.{MapSqlParameterSource, NamedParameterJdbcTemplate}
import org.springframework.stereotype.Repository

import com.sbuslab.utils.db.JacksonBeanRowMapper

import co.copper.test.datamodel.User


@Repository
@Autowired
class TestRepository(jdbcTemplate: NamedParameterJdbcTemplate, mapper: ObjectMapper) {

  private val rowMapper = new JacksonBeanRowMapper(classOf[User], mapper)

  def getById(id: Long): util.List[User] =
    jdbcTemplate.query("""
      SELECT * FROM test
      WHERE id = :id
    """, new MapSqlParameterSource("id", id), rowMapper)
}
