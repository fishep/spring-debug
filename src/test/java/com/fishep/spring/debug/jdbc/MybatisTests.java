package com.fishep.spring.debug.jdbc;

import com.fishep.spring.debug.jdbc.mapper.DemoMapper;
import com.fishep.spring.debug.jdbc.po.Demo;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author fly.fei
 * @Date 2024/5/10 10:47
 * @Desc
 **/
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MybatisTests {

    @Autowired
    private DemoMapper mapper;

    private static Demo demo = new Demo(0L, "this is a test from Mybatis");

    @Test
    @Order(1)
    void insert() {
        Boolean flag = mapper.insert(demo);

        assertTrue(flag);
        assertThat(demo.getId(), greaterThan(0L));
        assertSameWithDb();
    }

    @Test
    @Order(4)
    void delete() {
        Boolean flag = mapper.delete(demo);
        assertTrue(flag);
    }

    @Test
    @Order(3)
    void update() {
        demo.setComment("update comment");

        Boolean flag = mapper.update(demo);
        assertTrue(flag);

        assertSameWithDb();
    }

    @Test
    @Order(2)
    void select() {
        assertSameWithDb();
    }

    void assertSameWithDb(){
        Demo demo1 = mapper.select(demo);
        assertNotNull(demo1);
        assertNotSame(demo1, demo);
        assertEquals(demo.getId(), demo1.getId());
        assertEquals(demo.getComment(), demo1.getComment());
    }

}
