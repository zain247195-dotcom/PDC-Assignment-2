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
double parallelTotal = pool.invoke(
    new ParallelRiskCalculator(assets, 0, assets.length)
);
long parallelTime = System.nanoTime() - parallelStart;
    }
}