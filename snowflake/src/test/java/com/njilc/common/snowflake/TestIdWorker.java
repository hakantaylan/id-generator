package com.njilc.common.snowflake;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class TestIdWorker {
    private static final Logger logger = LoggerFactory.getLogger(TestIdWorker.class);

    static class IdWorkerThread implements Runnable {
        private Set<Long> set;
        private IdWorker idWorker;

        public IdWorkerThread(Set<Long> set, IdWorker idWorker) {
            this.set = set;
            this.idWorker = idWorker;
        }

        @Override
        public void run() {
            while (true) {
                long id = idWorker.nextId();
                logger.info("id: {}", id);
                if (!set.add(id)) {
                    logger.error("duplicate: {}", id);
                }
            }
        }
    }

    @Test
    public void testNextId() {
        Set<Long> set = new HashSet<Long>();
        final IdWorker idWorker1 = new IdWorker(0, 0);
        final IdWorker idWorker2 = new IdWorker(1, 0);
        Thread thread1 = new Thread(new IdWorkerThread(set, idWorker1));
        Thread thread2 = new Thread(new IdWorkerThread(set, idWorker2));
        thread1.setDaemon(true);
        thread2.setDaemon(true);
        thread1.start();
        thread2.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}