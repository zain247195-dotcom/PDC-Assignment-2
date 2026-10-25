import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

public class InventoryCatalogManager {
    private static final int WORKERS = 10;
    private static final int OPERATIONS_PER_WORKER = 50000;
    private static final int PRODUCT_COUNT = 1000;

    private Map<String, Integer> createInitialCatalog() {
        Map<String, Integer> catalog = new HashMap<>();
        for (int i = 0; i < PRODUCT_COUNT; i++) {
            catalog.put("P" + i, 100);
        }
        return catalog;
    }

    public void benchmarkSynchronizedMap() throws InterruptedException {
        Map<String, Integer> catalog = Collections.synchronizedMap(createInitialCatalog());
        runBenchmark("Collections.synchronizedMap", catalog, false);
    }

    public void benchmarkConcurrentMap() throws InterruptedException {
        Map<String, Integer> catalog = new ConcurrentHashMap<>(createInitialCatalog());
        runBenchmark("ConcurrentHashMap", catalog, true);
    }

    private void runBenchmark(String label, Map<String, Integer> catalog, boolean concurrentMap) throws InterruptedException {
        CountDownLatch ready = new CountDownLatch(WORKERS);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(WORKERS);
        AtomicLong completed = new AtomicLong();

        for (int worker = 0; worker < WORKERS; worker++) {
            final int workerId = worker;
            Thread thread = new Thread(() -> {
                ready.countDown();
                try {
                    start.await();
                    for (int i = 0; i < OPERATIONS_PER_WORKER; i++) {
                        String productId = "P" + ((i + workerId) % PRODUCT_COUNT);
                        if (concurrentMap) {
                            catalog.compute(productId, (key, value) -> value == null ? 1 : value + 1);
                        } else {
                            synchronized (catalog) {
                                Integer value = catalog.get(productId);
                                catalog.put(productId, value == null ? 1 : value + 1);
                            }
                        }
                        completed.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            }, label.replace('.', '-') + "-" + worker);
            thread.start();
        }

        ready.await();
        long startTime = System.nanoTime();
        start.countDown();
        done.await();
        long elapsed = System.nanoTime() - startTime;
        double seconds = elapsed / 1_000_000_000.0;
        System.out.println("Benchmark: " + label);
        System.out.println("Worker threads: " + WORKERS);
        System.out.println("Completed operations: " + completed.get());
        System.out.printf("Execution time: %.3f ms%n", elapsed / 1_000_000.0);
        System.out.printf("Throughput: %.0f operations/second%n%n", completed.get() / seconds);
    }

    public static void main(String[] args) throws InterruptedException {
        InventoryCatalogManager manager = new InventoryCatalogManager();
        manager.benchmarkSynchronizedMap();
        manager.benchmarkConcurrentMap();
    }
}
