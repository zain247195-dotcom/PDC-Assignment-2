public class ParallelMinSearch extends SensorSearchEngine {
    public ParallelMinSearch(double[] sensorData, int startIdx, int endIdx) {
        super(sensorData, startIdx, endIdx);
    }

    @Override
    public double processGrid() {
        double minimum = Double.POSITIVE_INFINITY;
        for (int i = startIdx; i < endIdx; i++) {
            if (sensorData[i] < minimum) {
                minimum = sensorData[i];
            }
        }
        return minimum;
    }
}
