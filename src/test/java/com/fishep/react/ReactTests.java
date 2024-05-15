package com.fishep.react;

import cn.hutool.core.lang.Pair;
import com.fishep.testfixture.react.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * @Author fly.fei
 * @Date 2024/4/15 14:15
 * @Desc
 **/
@Slf4j
public class ReactTests {

    @Test
    void blockLast() {
        PersonDao personDao = new PersonDao();

        Flux<Person> peopleFlux = personDao.rxListPeople();

        log.info("------------------");

        Flux<List<Person>> listFlux = peopleFlux.buffer().log();

        log.info("------------------");

        List<Person> people = listFlux.blockLast(Duration.ofSeconds(3));

        log.info("------------------");

        assert people != null;
        people.forEach(person -> log.info(person.toString()));
    }

    @Test
    void deferBlockLast() {
        PersonDao personDao = new PersonDao();

        Flux<Person> peopleFlux = personDao.rxDeferListPeople();

        log.info("------------------");

        Flux<List<Person>> listFlux = peopleFlux.buffer().log();

        log.info("------------------");

        List<Person> people = listFlux.blockLast(Duration.ofSeconds(3));

        log.info("------------------");

        assert people != null;
        people.forEach(person -> log.info(person.toString()));
    }

    @Test
    void fun() {
        TicketService ticketService = new TicketService();

        Flight flight = ticketService.lookupFlight("LOT 783");
        Passenger passenger = ticketService.findPassenger(1L);
        Ticket ticket = ticketService.bookTicket(flight, passenger);
        ticketService.sendEmail(ticket);
    }

    @Test
    void react() {
        TicketService ticketService = new TicketService();

        Mono<Flight> flight = ticketService.rxLookupFlight("LOT 783");
        Mono<Passenger> passenger = ticketService.rxFindPassenger(1L);
        Mono<Ticket> ticket = flight.zipWith(passenger, ticketService::bookTicket);
        ticket.subscribe(ticketService::sendEmail);
    }

    @Test
    void subscribeOn() throws InterruptedException {
        TicketService ticketService = new TicketService();

        Mono<Flight> flight = ticketService.rxLookupFlight("LOT 783").subscribeOn(Schedulers.boundedElastic());
        Mono<Passenger> passenger = ticketService.rxFindPassenger(1L).subscribeOn(Schedulers.boundedElastic()).timeout(Duration.ofSeconds(3)); // 还可以声明一个超时

//        Mono<Mono<Ticket>> ticket = flight.zipWith(passenger, (f, p) -> ticketService.rxBookTicket(f, p));
        Mono<Ticket> ticket = flight.zipWith(passenger, (f, p) -> ticketService.rxBookTicket(f, p)).flatMap(abs -> abs);
        ticket.subscribe(ticketService::sendEmail);

    }

    @Test
    void asyncTasks() {
        TicketService ticketService = new TicketService();
        ArrayList<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket());
        tickets.add(new Ticket());
        tickets.add(new Ticket());

        List<Pair<Ticket, Future<Boolean>>> tasks = tickets.stream().map(ticket -> Pair.of(ticket, ticketService.sendEmailAsync(ticket))).toList();

        List<Ticket> failures = tasks.stream().flatMap(pair -> {
            try {
                Future<Boolean> future = pair.getValue();
                Boolean aBoolean = future.get();
                log.info("sendEmail " + aBoolean);
                return Stream.empty();
            } catch (Exception e) {
                Ticket ticket = pair.getKey();
                log.warn("Failed to send {}", ticket, e);
                return Stream.of(ticket);
            }
        }).toList();

    }

//    @Test
//    void asyncTasks() {
//        TicketService ticketService = new TicketService();
//        ArrayList<Ticket> tickets = new ArrayList<>();
//        tickets.add(new Ticket());
//        tickets.add(new Ticket());
//        tickets.add(new Ticket());
//        List<Ticket> failures = tickets.stream().map(ticket -> Pair.of(ticket, ticketService.sendEmailAsync(ticket))).flatMap(pair -> {
//            try {
//                Future<Boolean> future = pair.getValue();
//                Boolean aBoolean = future.get();
//                log.info("sendEmail " + aBoolean);
//                return Stream.empty();
//            } catch (Exception e) {
//                Ticket ticket = pair.getKey();
//                log.warn("Failed to send {}", ticket, e);
//                return Stream.of(ticket);
//            }
//        }).toList();
//    }

    @Test
    void reactTasks() {
        TicketService ticketService = new TicketService();
        ArrayList<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket());
        tickets.add(new Ticket());
        tickets.add(new Ticket());

        List<Ticket> failures = Flux.fromIterable(tickets)
            .flatMap(ticket ->
                ticketService.rxSendEmail(ticket)
                    .ignoreElement()
                    .ofType(Ticket.class)
                    .doOnError(e -> log.warn("Failed to send {}", ticket, e))
                    .onErrorReturn(ticket))
            .buffer()
            .blockLast();
    }

    @Test
    void asyncReactTasks() {
        TicketService ticketService = new TicketService();
        ArrayList<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket());
        tickets.add(new Ticket());
        tickets.add(new Ticket());

        List<Ticket> failures = Flux.fromIterable(tickets)
            .flatMap(ticket ->
                ticketService.rxSendEmail(ticket)
                    .ignoreElement()
                    .ofType(Ticket.class)
                    .doOnError(e -> log.warn("Failed to send {}", ticket, e))
                    .onErrorReturn(ticket)
                    .subscribeOn(Schedulers.boundedElastic())
            )
            .buffer()
            .blockLast();
    }

}
