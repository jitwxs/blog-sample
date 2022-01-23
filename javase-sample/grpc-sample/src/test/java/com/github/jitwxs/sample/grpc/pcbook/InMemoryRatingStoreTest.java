package com.github.jitwxs.sample.grpc.pcbook;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

public class InMemoryRatingStoreTest {

    @Test
    public void add() throws InterruptedException {
        InMemoryRatingStore ratingStore = new InMemoryRatingStore();

        List<Callable<Rating>> tasks = new LinkedList<>();
        String laptopID = UUID.randomUUID().toString();
        double score = 5;

        int n = 10;
        for (int i = 0; i < n; i++) {
            tasks.add(() -> ratingStore.Add(laptopID, score));
        }

        Set<Integer> ratedCount = new HashSet<>();
        Executors.newWorkStealingPool()
                .invokeAll(tasks)
                .stream()
                .forEach(future -> {
                    try {
                        Rating rating = future.get();
                        assertEquals(rating.getSum(), rating.getCount() * score, 1e-9);
                        ratedCount.add(rating.getCount());
                    } catch (Exception e) {
                        throw new IllegalStateException();
                    }
                });

        assertEquals(n, ratedCount.size());
        for (int cnt = 1; cnt <= n; cnt++) {
            assertTrue(ratedCount.contains(cnt));
        }
    }
}