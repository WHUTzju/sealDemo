package cn.hyperchain.abs;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: sealDemo
 * @description:
 * @author: inkChain
 * @create: 2023-03-23 13:03
 **/
public class AQSMain {

    /**
     * Java AQS（AbstractQueuedSynchronizer）是Java并发包中的一个重要组件，它提供了一种实现同步和互斥的机制。
     * 它是一个抽象的同步器，可以用来实现各种同步器，如ReentrantLock、Semaphore、CountDownLatch等。
     * <p>
     * AQS的核心思想是使用一个FIFO队列来管理线程的状态，使用CAS（Compare And Swap）操作来保证线程安全。
     * AQS维护了一个state变量，用于表示同步状态，当state为0时表示没有线程持有同步资源，当state不为0时表示有线程持有同步资源。
     * <p>
     * 当多个线程同时请求同步资源时，AQS会将这些线程加入到一个FIFO队列中，然后只有队列头的线程可以获取同步资源，其他线程会被阻塞。当队列头的线程释放同步资源后，AQS会唤醒队列中的下一个线程，使其获取同步资源。
     * <p>
     * AQS的实现依赖于两个重要的方法：tryAcquire和tryRelease。tryAcquire方法用于尝试获取同步资源，如果获取成功则返回true，否则返回false。tryRelease方法用于释放同步资源。
     * <p>
     * AQS还提供了一些其他的方法，如acquire、release、acquireShared、releaseShared等，这些方法都是基于tryAcquire和tryRelease方法实现的。
     * <p>
     * 总之，AQS是Java并发包中的一个重要组件，它提供了一种实现同步和互斥的机制，可以用于实现各种同步器。
     */

    private static Semaphore semaphore = new Semaphore(5);

    public static void main(String[] args) {
        /**
         * Semaphore start 信号量
         */
        if (!semaphore.tryAcquire()) {
            System.out.println("木有位置，持续等待下载任务进行中,,,");
            //
        }
        try {

        } finally {
            semaphore.release();
        }
        /**
         * Semaphore end 信号量
         */


        /**
         *  ReentrantLock start 可重入锁 实现公平锁/非公平锁
         */

        ReentrantLock lock = new ReentrantLock(false);
        // 2.可用于代码块
        lock.lock();
        try {

            // 3.支持多种加锁方式，比较灵活; 具有可重入特性
            if (lock.tryLock(100, TimeUnit.MILLISECONDS)) {
                //TODO
            }
            // 4.手动释放锁
            lock.unlock();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }


    }
}
