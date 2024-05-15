package com.fishep.testfixture.react;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @Author fly.fei
 * @Date 2024/4/15 14:46
 * @Desc
 **/
@Slf4j
public class TicketService {

    ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    /**
     * 查询航班
     *
     * @param flightNo
     * @return
     */
    public Flight lookupFlight(String flightNo) {
        log.trace("lookupFlight");

        return new Flight(flightNo);
    }

    /**
     * 查询乘客
     *
     * @param id
     * @return
     */
    public Passenger findPassenger(Long id) {
        log.trace("findPassenger");

        return new Passenger(id);
    }

    /**
     * 根据航班和乘客订票
     *
     * @param flight
     * @param passenger
     * @return
     */
    public Ticket bookTicket(Flight flight, Passenger passenger) {
        log.trace("bookTicket");

        return new Ticket(flight, passenger);
    }

    /**
     * 发送邮件
     *
     * @param ticket
     * @return
     */
    public boolean sendEmail(Ticket ticket) {
        log.trace("sendEmail");

        return true;
    }

    public Mono<Flight> rxLookupFlight(String flightNo) {
        return Mono.defer(() -> Mono.just(lookupFlight(flightNo)));
    }

    public Mono<Passenger> rxFindPassenger(Long id) {
        return Mono.defer(() -> Mono.just(findPassenger(id)));
    }

    public Mono<Ticket> rxBookTicket(Flight flight, Passenger passenger) {
        return Mono.defer(() -> Mono.just(bookTicket(flight, passenger)));
    }

    public Mono<Boolean> rxSendEmail(Ticket ticket) {

        log.trace("rxSendEmail");

        return Mono.fromCallable(() -> sendEmail(ticket));
    }

    public Future<Boolean> sendEmailAsync(Ticket ticket) {

        log.trace("sendEmailAsync");

        return pool.submit(() -> sendEmail(ticket));
    }

}
