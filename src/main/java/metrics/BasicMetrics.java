package metrics;

import java.util.HashMap;
import java.util.Map;

public class BasicMetrics implements Metrics {
    private long startTime;
    private long elapsedTime;
    private final Map<String, Long> counters = new HashMap<>();

    @Override
    public void startTimer() {
        startTime = System.nanoTime();
    }

    @Override
    public void stopTimer() {
        elapsedTime = System.nanoTime() - startTime;
    }

    @Override
    public long getElapsedTime() {
        return elapsedTime;
    }

    @Override
    public void incrementCounter(String name) {
        counters.put(name, counters.getOrDefault(name, 0L) + 1);
    }

    @Override
    public long getCounter(String name) {
        return counters.getOrDefault(name, 0L);
    }

    @Override
    public void reset() {
        counters.clear();
        elapsedTime = 0;
    }
}
