package com.fishep.testfixture.react;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author fly.fei
 * @Date 2024/4/15 14:16
 * @Desc
 **/
@Slf4j
public class PersonDao {

    public List<Person> listPeople() {
        return query("select * from people");
    }

    public Flux<Person> rxListPeople() {
        return Flux.fromIterable(query("select * from people"));
    }

    public Flux<Person> rxDeferListPeople() {
        return Flux.defer(() ->
                Flux.fromIterable(query("select * from people")));
    }

    private List<Person> query(String sql) {
        log.info("query data from database;");

        List<Person> people = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Person person = new Person();
            person.setId(i);
            person.setName("name" + i);
            people.add(person);
        }

        return people;
    }

}
