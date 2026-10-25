public class CacheLocalityBenchmark {
    private static final int N = 2000;
    private static final int REPEATS = 10;
    private double[][] matrix = new double[N][N];
    private volatile double resultSink;

    public CacheLocalityBenchmark() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                matrix[i][j] = (i + j) * 0.5;
            }
        }
    }

    public double traverseRowMajor() {
        double sum = 0.0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                sum += matrix[i][j];
            }
        }
        return sum;
    }

    public double traverseColumnMajor() {
        double sum = 0.0;
        for (int j = 0; j < N; j++) {
            for (int i = 0; i < N; i++) {
                sum += matrix[i][j];
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        CacheLocalityBenchmark benchmark = new CacheLocalityBenchmark();
        for (int i = 0; i < 2; i++) {
            benchmark.resultSink = benchmark.traverseRowMajor();
            benchmark.resultSink = benchmark.traverseColumnMajor();
        }

        long rowTotal = 0L;
        long columnTotal = 0L;
        for (int i = 0; i < REPEATS; i++) {
            long start = System.nanoTime();
            benchmark.resultSink = benchmark.traverseRowMajor();
            rowTotal += System.nanoTime() - start;

            start = System.nanoTime();
            benchmark.resultSink = benchmark.traverseColumnMajor();
            columnTotal += System.nanoTime() - start;
        }

        double rowMs = rowTotal / 1_000_000.0 / REPEATS;
        double columnMs = columnTotal / 1_000_000.0 / REPEATS;
        System.out.println("Matrix size: " + N + " x " + N);
        System.out.println("Repeated iterations: " + REPEATS);
        System.out.printf("Average row-major time: %.3f ms%n", rowMs);
        System.out.printf("Average column-major time: %.3f ms%n", columnMs);
        System.out.printf("Row-major speedup over column-major: %.3fx%n", columnMs / Math.max(rowMs, 0.000001));
        System.out.printf("Checksum: %.2f%n", benchmark.resultSink);
    }
}
