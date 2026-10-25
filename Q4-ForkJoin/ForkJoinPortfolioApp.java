import java.util.concurrent.ForkJoinPool;
import java.util.Random;

public class ForkJoinPortfolioApp {
    public static void main(String[] args) {
        double[] assets = new double[10000];
        Random random = new Random(42);
        for (int i = 0; i < assets.length; i++) {
            assets[i] = 50.0 + random.nextDouble() * 150.0;
        }

        ForkJoinPool pool = new ForkJoinPool();
        long serialStart = System.nanoTime();
        double serialTotal = 0.0;
        for (double asset : assets) {
            serialTotal += asset;
        }
        long serialTime = System.nanoTime() - serialStart;

        long parallelStart = System.nanoTime();
        double parallelTotal = pool.invoke(new ParallelRiskCalculator(assets, 0, assets.length));
        long parallelTime = System.nanoTime() - parallelStart;

        pool.invoke(new PortfolioThresholdTask(assets, 0, assets.length, 170.0));
        double stressedTotal = pool.invoke(new ParallelRiskCalculator(assets, 0, assets.length));

        System.out.printf("Asset count: %d%n", assets.length);
        System.out.printf("Serial risk total: %.2f%n", serialTotal);
        System.out.printf("Parallel risk total before stress: %.2f%n", parallelTotal);
        System.out.printf("Risk total after 5%% stress adjustment: %.2f%n", stressedTotal);
        System.out.printf("Serial time: %.3f ms%n", serialTime / 1_000_000.0);
        System.out.printf("Parallel time: %.3f ms%n", parallelTime / 1_000_000.0);
        System.out.printf("Measured speedup (serial/parallel): %.2fx%n", (double) serialTime / Math.max(1L, parallelTime));
        pool.shutdown();
    }
}
