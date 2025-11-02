package graph.dagsp;

import metrics.BasicMetrics; // ✅ импортируем систему метрик
import java.util.*;

public class DAGShortestPaths {
    private final int n;
    private final List<List<Edge>> adj;
    private final BasicMetrics metrics = new BasicMetrics(); // ✅ объект метрик

    public static class Edge {
        int to, w;
        Edge(int to, int w) { this.to = to; this.w = w; }
    }

    public DAGShortestPaths(int n) {
        this.n = n;
        adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    }

    public void addEdge(int u, int v, int w) { adj.get(u).add(new Edge(v, w)); }

    // ✅ Кратчайшие пути (с метриками)
    public int[] shortestPath(int src, List<Integer> topo) {
        metrics.reset();
        metrics.startTimer();

        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;

        for (int u : topo) {
            if (dist[u] != Integer.MAX_VALUE) {
                for (Edge e : adj.get(u)) {
                    metrics.incrementCounter("relax_attempts");
                    if (dist[e.to] > dist[u] + e.w) {
                        dist[e.to] = dist[u] + e.w;
                        metrics.incrementCounter("relax_success");
                    }
                }
            }
        }

        metrics.stopTimer();
        return dist;
    }

    // ✅ Длиннейшие пути (с метриками)
    public int[] longestPath(int src, List<Integer> topo) {
        metrics.reset();
        metrics.startTimer();

        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MIN_VALUE);
        dist[src] = 0;

        for (int u : topo) {
            if (dist[u] != Integer.MIN_VALUE) {
                for (Edge e : adj.get(u)) {
                    metrics.incrementCounter("relax_attempts_longest");
                    if (dist[e.to] < dist[u] + e.w) {
                        dist[e.to] = dist[u] + e.w;
                        metrics.incrementCounter("relax_success_longest");
                    }
                }
            }
        }

        metrics.stopTimer();
        return dist;
    }

    // ✅ Вывод статистики для отчёта
    public void printMetrics(String type) {
        System.out.println("DAG " + type + " Path metrics:");
        System.out.println("  Time (ns): " + metrics.getElapsedTime());

        if (type.equalsIgnoreCase("Shortest")) {
            System.out.println("  Relax attempts: " + metrics.getCounter("relax_attempts"));
            System.out.println("  Relax success:  " + metrics.getCounter("relax_success"));
        } else {
            System.out.println("  Relax attempts: " + metrics.getCounter("relax_attempts_longest"));
            System.out.println("  Relax success:  " + metrics.getCounter("relax_success_longest"));
        }
    }

    public BasicMetrics getMetrics() {
        return metrics;
    }
}
