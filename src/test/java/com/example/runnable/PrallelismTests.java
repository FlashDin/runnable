package com.example.runnable;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

class PrallelismTests {

    private static Logger logger = LoggerFactory.getLogger(PrallelismTests.class);

    @Test
    void sequencialStream() {
        List<Integer> listOfNumbers = Arrays.asList(1, 2, 3, 4);
        listOfNumbers.stream().forEach(number ->
                System.out.println(number + " " + Thread.currentThread().getName())
        );
    }

    @Test
    void parallelStream() {
        List<Integer> listOfNumbers = Arrays.asList(1, 2, 3, 4);
        listOfNumbers.parallelStream().forEach(number ->
                System.out.println(number + " " + Thread.currentThread().getName())
        );
    }

    @Test
    void splittingSource() {
        List<Integer> listOfNumbers = Arrays.asList(1, 2, 3, 4);
        int sum = listOfNumbers.parallelStream().reduce(5, Integer::sum);
        assertThat(sum).isNotEqualTo(15);

        listOfNumbers = Arrays.asList(1, 2, 3, 4);
        sum = listOfNumbers.parallelStream().reduce(0, Integer::sum) + 5;
        assertThat(sum).isEqualTo(15);
    }

    @Test
    void customThreadPool() throws ExecutionException, InterruptedException {
        List<Integer> listOfNumbers = Arrays.asList(1, 2, 3, 4);
        ForkJoinPool customThreadPool = new ForkJoinPool(4);
        int sum = customThreadPool.submit(
                () -> listOfNumbers.parallelStream().reduce(0, Integer::sum)).get();
        customThreadPool.shutdown();
        assertThat(sum).isEqualTo(10);
    }

    @Test
    void sequential() {
        for (int i = 0; i < 5; i++) {
            System.out.println("t0: " + i);
        }
    }

    @Test
    void thread() throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    System.out.println("t1: " + i);
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    System.out.println("t2: " + i);
                }
            }
        });

        t1.start();
//		t1.sleep(3000);
        t2.start();
    }

    @Test
    void future() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Future<String> future = executorService.submit(() -> {
            Thread.sleep(10000l);
            return "Hehe";
        });

        if (future.isDone() && !future.isCancelled()) {
            try {
                String str = future.get();
                System.out.println(str);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void execution() {
        Executor executor = new Executor() {
            @Override
            public void execute(Runnable command) {
                System.out.println("HEHE");
                command.run();
            }
        };
    }

    @Test
    void runScheduler() throws ParseException {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Jakarta"));
        ZonedDateTime nextRun = now.withHour(8).withMinute(49).withSecond(0);
        if(now.compareTo(nextRun) > 0) {
            nextRun = nextRun.plusDays(1);
        }
        Duration duration = Duration.between(now, nextRun);
        long initalDelay = duration.getSeconds();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new Runnable() {
                                          @Override
                                          public void run() {
                                              System.out.println("HEHE");
                                          }
                                      },
                initalDelay,
                TimeUnit.DAYS.toSeconds(1),
                TimeUnit.SECONDS);
    }

    @Test
    void ngelogger() {
        logger.info("HEHE", "HAHA");
    }

}
