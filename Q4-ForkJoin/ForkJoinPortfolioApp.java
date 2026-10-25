import java.util.Random;

public class ForkJoinPortfolioApp {
    public static void main(String[] args) {
        double[] assets = new double[10000];
        Random random = new Random(42);

        for (int i = 0; i < assets.length; i++) {
            assets[i] = 50.0 + random.nextDouble() * 150.0;
        }
    }
}