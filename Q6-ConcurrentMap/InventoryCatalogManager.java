import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

public class InventoryCatalogManager {
    public void benchmarkSynchronizedMap() throws InterruptedException {
        Map<String, Integer> catalog =
            Collections.synchronizedMap(createInitialCatalog());
        runBenchmark("Collections.synchronizedMap", catalog, false);
    }
    public void benchmarkConcurrentMap() throws InterruptedException {
        Map<String, Integer> catalog =
            new ConcurrentHashMap<>(createInitialCatalog());
        runBenchmark("ConcurrentHashMap", catalog, true);
    }
}
