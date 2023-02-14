package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class InfoService {
    private final Logger logger = LoggerFactory.getLogger(InfoService.class);

    public Integer getSumOfNumbers(int num) {
        logger.info("Was invoked method to get the sum of {} numbers", num);

        long startTime1 = System.nanoTime();
        int result1 = Stream.iterate(1, a -> a + 1)
                .limit(num)
                .reduce(0, Integer::sum);
        long finishTime1 = System.nanoTime();
        long delta1 = finishTime1 - startTime1;

        long startTime2 = System.nanoTime();
        int result2 = IntStream.range(1, num + 1)
                .sum();
        long finishTime2 = System.nanoTime();
        long delta2 = finishTime2 - startTime2;

        if (delta2 < delta1) {
            logger.info("Progress {} ms", delta1 - delta2);
        } else {
            logger.info("Regress {} ms", delta2 - delta1);
        }
        if (result1 != result2) {
            logger.error("{} != {}", result1, result2);
        }

        return result2;
    }
}
