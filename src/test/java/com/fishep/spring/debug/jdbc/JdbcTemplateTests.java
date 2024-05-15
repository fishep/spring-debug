package com.fishep.spring.debug.jdbc;

import com.fishep.spring.debug.jdbc.po.Demo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author fly.fei
 * @Date 2024/5/9 14:29
 * @Desc
 **/
@Slf4j
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JdbcTemplateTests {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static Demo demo = new Demo(0L, "this is a test from jdbcTemplate");

    @Test
    @Order(1)
    void insert() {
        String sql = "insert into demo(comment) values (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int flag = jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, demo.getComment());
            return stmt;
        }, keyHolder);

        assertEquals(1, flag);
        assertNotNull(keyHolder.getKey());
        demo.setId(keyHolder.getKey().longValue());

        assertThat(demo.getId(), greaterThan(0L));
        assertSameWithDb();
    }

    @Test
    @Order(8)
    void delete() {
        String sql = "delete from demo where id = ?";

        int flag = jdbcTemplate.update(sql, demo.getId());

        assertEquals(1, flag);
    }

    @Test
    @Order(2)
    void update() {
        demo.setComment("update comment");

        String sql = "update demo set comment = ? where id = ?";

        int flag = jdbcTemplate.update(sql, demo.getComment(), demo.getId());

        assertEquals(1, flag);
        assertSameWithDb();
    }

    @Test
    @Order(3)
    void execute() throws SQLException {
        String sql = "select * from demo where id = ?";

        Demo demo1 = jdbcTemplate.execute(sql, (PreparedStatementCallback<Demo>) ps -> {
            ps.setLong(1, demo.getId());
            ResultSet rs = ps.executeQuery();

            assertSameWithResultSet(rs);

            return mapDemo(rs);
        });

        assertSameWithObject(demo1);
    }

    @Test
    @Order(4)
    void queryForResultSetExtractor() throws SQLException {
        String sql = "select * from demo where id = " + demo.getId();

        Demo demo1 = jdbcTemplate.query(sql, (ResultSetExtractor<Demo>) rs -> {
            assertSameWithResultSet(rs);
            return mapDemo(rs);
        });

        assertSameWithObject(demo1);
    }

    @Test
    @Order(5)
    void queryForRowMapper() throws SQLException {
        String sql = "select * from demo where id = " + demo.getId();

        List<Demo> list = jdbcTemplate.query(sql, (RowMapper<Demo>)(rs, rowNum) -> {
//            只有执行了 rs.next() 才能获取获取到 row , 所以在 RowMapper 中不能执行 rs.next()
//            assertSameWithResultSet(rs);
            assertEquals(demo.getId(), rs.getLong("id"));
            assertEquals(demo.getComment(), rs.getString("comment"));

            return mapDemo(rs);
        });

        assertEquals(1, list.size());
        assertSameWithObject(list.get(0));
    }

    @Test
    @Order(6)
    void queryForObject() {
        String sql = "select comment from demo where id = " + demo.getId();

        String comment = jdbcTemplate.queryForObject(sql, String.class);

        assertNotNull(comment);
        assertEquals(demo.getComment(), comment);
    }

    @Test
    @Order(7)
    void queryForList() {
        String sql = "select comment from demo";

        List<String> list = jdbcTemplate.queryForList(sql, String.class);

        assertThat(list.size(), greaterThanOrEqualTo(1));
    }

    void assertSameWithDb() {
        String sql = "select * from demo where id = " + demo.getId();

        jdbcTemplate.query(sql, (ResultSetExtractor<Demo>) rs -> {
            assertSameWithResultSet(rs);
            return null;
        });
    }

    private static void assertSameWithResultSet(ResultSet rs) throws SQLException {
        assertTrue(rs.next());
        assertEquals(demo.getId(), rs.getLong("id"));
        assertEquals(demo.getComment(), rs.getString("comment"));
    }

    private static void assertSameWithObject(Demo d) throws SQLException {
        assertNotNull(d);
        assertEquals(demo.getId(), d.getId());
        assertEquals(demo.getComment(), d.getComment());
    }

    private static Demo mapDemo(ResultSet rs) throws SQLException {
        Demo demo = new Demo();
        demo.setId(rs.getLong("id"));
        demo.setComment(rs.getString("comment"));
        return demo;
    }

}
