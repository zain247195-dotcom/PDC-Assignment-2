import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SensorGridMonitorApp {
    public static void main(String[] args) throws Exception {
        int count = 50000;
        int workers = 4;
        double[] data = new double[count];
        Random random = new Random(42);
        for (int i = 0; i < count; i++) {
            data[i] = -10.0 + random.nextDouble() * 110.0;
        }

        ExecutorService pool = Executors.newFixedThreadPool(workers);
        List<Future<Double>> minResults = new ArrayList<>();
        List<Future<Double>> maxResults = new ArrayList<>();
        int chunk = (count + workers - 1) / workers;
        long startTime = System.nanoTime();

        for (int worker = 0; worker < workers; worker++) {
            int start = worker * chunk;
            int end = Math.min(start + chunk, count);
            if (start < end) {
                minResults.add(pool.submit(() -> new ParallelMinSearch(data, start, end).processGrid()));
                maxResults.add(pool.submit(() -> new ParallelMaxSearch(data, start, end).processGrid()));
            }
        }

        double globalMin = Double.POSITIVE_INFINITY;
        double globalMax = Double.NEGATIVE_INFINITY;
        for (Future<Double> result : minResults) {
            globalMin = Math.min(globalMin, result.get());
        }
        for (Future<Double> result : maxResults) {
            globalMax = Math.max(globalMax, result.get());
        }

        long elapsed = System.nanoTime() - startTime;
        pool.shutdown();
        System.out.println("Sensor readings processed: " + count);
        System.out.printf("Global minimum temperature: %.3f%n", globalMin);
        System.out.printf("Global maximum vibration value: %.3f%n", globalMax);
        System.out.printf("Worker threads: %d%n", workers);
        System.out.printf("Execution time: %.3f ms%n", elapsed / 1_000_000.0);
    }
}
