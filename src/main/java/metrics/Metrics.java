package metrics;

public interface Metrics {
    void startTimer();
    void stopTimer();
    long getElapsedTime();
    void incrementCounter(String name);
    long getCounter(String name);
    void reset();
}
