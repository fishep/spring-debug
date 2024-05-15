package com.fishep.java.jdbc;

import com.fishep.spring.debug.jdbc.po.Demo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.sql.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author fly.fei
 * @Date 2024/5/9 17:21
 * @Desc
 **/
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MysqlTests {

    private static String JDBC_URL = "jdbc:mysql://mysql.dev:3306/demo";
    private static String JDBC_USER = "demo";
    private static String JDBC_PASSWORD = "demo";

    private static Connection conn;

    private static Demo demo = new Demo(0L, "this is a test from JDBC");

    @BeforeAll
    static void beforeAll() throws SQLException {
        conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    @AfterAll
    static void afterAll() throws SQLException {
        conn.close();
    }

    @Test
    @Order(1)
    void insert() throws SQLException {
        String sql = "insert into demo(comment) values (?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, demo.getComment());
            int flag = stmt.executeUpdate();
            assertEquals(1, flag);

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1); // 注意：索引从1开始

                    demo.setId(id);
                }
            }
        }

        assertThat(demo.getId(), greaterThan(0L));
        assertSameWithDb();
    }

    @Test
    @Order(4)
    void delete() throws SQLException {
        String sql = "delete from demo where id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, demo.getId());

            int flag = stmt.executeUpdate();
            assertEquals(1, flag);
        }
    }

    @Test
    @Order(3)
    void update() throws SQLException {
        demo.setComment("update comment");

        String sql = "update `demo` set `comment`=? where `id` = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, demo.getComment());
            stmt.setLong(2, demo.getId());

            int flag = stmt.executeUpdate();
            assertEquals(1, flag);
        }

        assertSameWithDb();
    }

    @Test
    @Order(2)
    void select() throws SQLException {
        assertSameWithDb();
    }

    void assertSameWithDb() throws SQLException {
        String sql = "select * from demo where id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, demo.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(demo.getId(), rs.getLong("id"));
                assertEquals(demo.getComment(), rs.getString("comment"));
            }
        }
    }

}
